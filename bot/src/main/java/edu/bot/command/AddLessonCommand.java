package edu.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Lesson;
import edu.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static edu.bot.command.Command.ADD_LESSON;
import static edu.bot.util.MessagesUtils.INVALID_DATE_FORMAT;
import static edu.bot.util.MessagesUtils.LESSON_ADD_EXAMPLE;
import static edu.bot.util.MessagesUtils.LESSON_OVERLAP;

@Log4j2
@RequiredArgsConstructor
public class AddLessonCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isAddLessonCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /add_lesson command");
        return buildMessage(command, chatId);
    }

    private boolean isAddLessonCommand(String command) {
        return command.startsWith(ADD_LESSON.getName());
    }

    private SendMessage buildMessage(String command, long chatId) {
        String[] splitCommand = command.split(" ", 9);
        if (splitCommand.length < 9) {
            return new SendMessage(chatId, LESSON_ADD_EXAMPLE);
        }

        String firstName = splitCommand[1];
        String lastName = splitCommand[2];
        String middleName = splitCommand[3];
        String subject = splitCommand[4];

        boolean isPaid = splitCommand[5].equalsIgnoreCase("да");


        double price;
        int duration;
        try {
            price = Double.parseDouble(splitCommand[6]);
            duration = Integer.parseInt(splitCommand[7]);
        } catch (NumberFormatException e) {
            return new SendMessage(chatId, "Неверный формат цены или длительности.");
        }

        String dateTimeString = splitCommand[8];
        Date lessonDate = parseDate(dateTimeString);
        if (lessonDate == null) {
            return new SendMessage(chatId, INVALID_DATE_FORMAT);
        }

        if (!service.isStudentExists(chatId, firstName, lastName, middleName)) {
            return new SendMessage(chatId, "Студент с указанным ФИО не найден.");
        }

        if (service.isLessonOverlapping(chatId, lessonDate, duration)) {
            return new SendMessage(chatId, LESSON_OVERLAP);
        }

        String studentFullName = String.join(" ", firstName, lastName, middleName);
        Lesson lesson = new Lesson(UUID.randomUUID(), studentFullName, lessonDate, duration, subject, price, isPaid);
        service.addLesson(chatId, lesson);

        String paymentStatus = isPaid ? "да" : "нет";
        return new SendMessage(chatId, String.format("Занятие добавлено. Оплачено: %s", paymentStatus));
    }


    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);
        } catch (ParseException e) {
            log.warn("Invalid date format for lesson: " + dateString, e);
            return null;
        }
    }
}
