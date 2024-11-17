package edu.bot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import edu.bot.dto.Lesson;
import edu.bot.dto.Student;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class KeyboardBuilder {

    public static InlineKeyboardMarkup createLessonKeyboard(List<Lesson> lessons) {
        return new InlineKeyboardMarkup(
                lessons.stream()
                        .map(lesson -> new InlineKeyboardButton(lesson.getSubject())
                                .callbackData("/list_lessons:" + lesson.getId()))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static InlineKeyboardMarkup createStudentKeyboard(List<Student> students) {
        return new InlineKeyboardMarkup(
                students.stream()
                        .map(student -> new InlineKeyboardButton(student.getFirstName() + student.getLastName() + student.getMiddleName())
                                .callbackData("/student_list:" + student.getId()))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static InlineKeyboardMarkup createLessonDeleteKeyboard(List<Lesson> lessons) {
        return new InlineKeyboardMarkup(
                lessons.stream()
                        .map(lesson -> new InlineKeyboardButton(lesson.getSubject() + " (" + lesson.getDate() + ")")
                                .callbackData("/delete_lesson:" + lesson.getId()))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static InlineKeyboardMarkup createStudentDeleteKeyboard(List<Student> students) {
        return new InlineKeyboardMarkup(
                students.stream()
                        .map(student -> new InlineKeyboardButton(student.getFirstName() + student.getLastName() + student.getMiddleName())
                                .callbackData("/delete_student:" + student.getId()))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static InlineKeyboardMarkup createLessonKeyboardForReschedule(List<Lesson> lessons) {
        return new InlineKeyboardMarkup(
                lessons.stream()
                        .map(lesson -> new InlineKeyboardButton(lesson.getSubject() + " (" + lesson.getDate() + ")")
                                .callbackData("/reschedule_lesson:" + lesson.getId()))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static InlineKeyboardMarkup createPaymentKeyboard(List<Lesson> lessons) {
        return new InlineKeyboardMarkup(
                lessons.stream()
                        .map(lesson -> new InlineKeyboardButton(lesson.getSubject() + " - Оплата")
                                .callbackData("/mark_payment:" + lesson.getId()))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static InlineKeyboardMarkup createDiscUntrackKeyboard(List<String> links) {
        return new InlineKeyboardMarkup(
                links.stream()
                        .map(link -> new InlineKeyboardButton(link).callbackData("/untrack_disc:" + link))
                        .map(button -> new InlineKeyboardButton[]{button})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }
}

