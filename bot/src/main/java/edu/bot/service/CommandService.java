package edu.bot.service;

import edu.bot.dto.Lesson;
import edu.bot.dto.Payment;
import edu.bot.dto.Student;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommandService {

    private final Map<Long, List<Lesson>> lessonsMap = new HashMap<>();
    private final Map<Long, List<Student>> studentsMap = new HashMap<>();
    private final Map<Long, List<Payment>> paymentMap = new HashMap<>();

    private final Map<Long, UUID> selectedLessonMap = new HashMap<>();
    private final Map<Long, Boolean> awaitingNewDateInputMap = new HashMap<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private final Map<Long, List<String>> trackedFoldersMap = new HashMap<>();
    private final Map<Long, Boolean> awaitingIncomeReportMap = new HashMap<>();


    public void trackGoogleDisc(long chatId, String folderId) {
        trackedFoldersMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(folderId);
    }

    public void untrackGoogleDisc(long chatId, String folderId) {
        List<String> folders = trackedFoldersMap.get(chatId);
        if (folders != null) {
            folders.remove(folderId);
        }
    }

    public List<String> getTrackedFolders(long chatId) {
        return trackedFoldersMap.getOrDefault(chatId, new ArrayList<>());
    }

    public void markPayment(long chatId, UUID lessonId) {
        lessonsMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .filter(lesson -> lesson.getId().equals(lessonId) && !lesson.isPaid())  // Проверка на оплату
                .findFirst()
                .ifPresent(lesson -> {
                    lesson.setPaid(true);  // Меняем статус на оплачено
                    paymentMap.computeIfAbsent(chatId, k -> new ArrayList<>())
                            .add(new Payment(UUID.randomUUID(), lessonId, new Date()));  // Добавляем оплату
                });
    }

    public boolean isLessonOverlapping(long chatId, Date newLessonDate, int newLessonDuration) {
        List<Lesson> lessons = lessonsMap.getOrDefault(chatId, new ArrayList<>());
        Date newLessonEnd = new Date(newLessonDate.getTime() + newLessonDuration * 60000);

        return lessons.stream().anyMatch(existingLesson -> {
            Date existingLessonEnd = new Date(existingLesson.getDate().getTime() + existingLesson.getDuration() * 60000);
            return newLessonDate.before(existingLessonEnd) && newLessonEnd.after(existingLesson.getDate());
        });
    }

    public String getPaymentsList(long chatId) {
        return paymentMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .map(payment -> {
                    Lesson lesson = lessonsMap.get(chatId).stream()
                            .filter(l -> l.getId().equals(payment.getLessonId()))
                            .findFirst().orElse(null);
                    double amount = (lesson != null) ? lesson.getPrice() : 0.0;
                    return "Оплата на сумму: " + decimalFormat.format(amount) + ", Дата: " + payment.getPaymentDate();
                })
                .collect(Collectors.joining("\n"));
    }

    public String getIncomeReport(long chatId, int month, int year) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM-yyyy");
        List<Lesson> allPaidLessons = lessonsMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .filter(Lesson::isPaid)
                .collect(Collectors.toList());

        double totalIncome = allPaidLessons.stream()
                .mapToDouble(Lesson::getPrice)
                .sum();

        return totalIncome > 0
                ? "Доход за " + monthYearFormat.format(new GregorianCalendar(year, month - 1, 1).getTime()) + ": " + decimalFormat.format(totalIncome) + " рублей."
                : "Нет данных о доходах за указанный месяц.";
    }

    public List<Payment> getLastFivePayments(long chatId) {
        List<Payment> payments = paymentMap.getOrDefault(chatId, new ArrayList<>());
        return payments.stream()
                .sorted(Comparator.comparing(Payment::getPaymentDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Lesson> getLessons(long chatId) {
        return lessonsMap.getOrDefault(chatId, new ArrayList<>());
    }

    public void addLesson(long chatId, Lesson lesson) {
        if (!isLessonOverlapping(chatId, lesson.getDate(), lesson.getDuration())) {
            lessonsMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(lesson);
        }
    }

    public void deleteLesson(long chatId, UUID lessonId) {
        lessonsMap.getOrDefault(chatId, new ArrayList<>())
                .removeIf(lesson -> lesson.getId().equals(lessonId));
    }

    public List<Student> getStudents(long chatId) {
        return studentsMap.getOrDefault(chatId, new ArrayList<>());
    }

    public void addStudent(long chatId, Student student) {
        if (!isStudentExists(chatId, student.getFirstName(), student.getLastName(), student.getMiddleName())) {
            studentsMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(student);
        }
    }

    public void deleteStudent(long chatId, UUID studentId) {
        String studentFullName = getStudentFullName(chatId, studentId);
        System.out.println("Удаление студента: " + studentFullName);

        studentsMap.getOrDefault(chatId, new ArrayList<>()).removeIf(student -> student.getId().equals(studentId));
        lessonsMap.getOrDefault(chatId, new ArrayList<>()).removeIf(lesson -> lesson.getStudentFullName().trim().equals(studentFullName.trim()));
    }

    private String getStudentFullName(long chatId, UUID studentId) {
        return studentsMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .filter(student -> student.getId().equals(studentId))
                .map(student -> String.join(" ", student.getFirstName(), student.getLastName(), student.getMiddleName()))
                .findFirst()
                .orElse("Неизвестный студент");
    }

    public void rescheduleLesson(long chatId, UUID lessonId, Date newDate) {
        List<Lesson> lessons = lessonsMap.getOrDefault(chatId, new ArrayList<>());

        int lessonDuration = getLessonDuration(chatId, lessonId);
        if (isLessonOverlapping(chatId, newDate, lessonDuration)) {
            throw new IllegalArgumentException("Невозможно перенести занятие, так как оно пересекается с другим занятием.");
        }
        lessons.stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .forEach(lesson -> lesson.setDate(newDate));
    }

    public int getLessonDuration(long chatId, UUID lessonId) {
        return lessonsMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .map(Lesson::getDuration)
                .findFirst()
                .orElse(0);
    }

    public void setSelectedLesson(long chatId, UUID lessonId) {
        selectedLessonMap.put(chatId, lessonId);
        awaitingNewDateInputMap.put(chatId, true);
    }

    public UUID getSelectedLesson(long chatId) {
        return selectedLessonMap.get(chatId);
    }


    public List<Lesson> getUnpaidLessons(long chatId) {
        return lessonsMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .filter(lesson -> !lesson.isPaid())
                .collect(Collectors.toList());
    }

    public boolean isStudentExists(long chatId, String firstName, String lastName, String middleName) {
        return studentsMap.getOrDefault(chatId, new ArrayList<>()).stream()
                .anyMatch(student -> student.getFirstName().equalsIgnoreCase(firstName) &&
                        student.getLastName().equalsIgnoreCase(lastName) &&
                        student.getMiddleName().equalsIgnoreCase(middleName));
    }

    public void setAwaitingIncomeReport(long chatId) {
        awaitingIncomeReportMap.put(chatId, true);
    }
}
