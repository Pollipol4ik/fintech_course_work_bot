package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Lesson;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static edu.bot.command.Command.RESCHEDULE_LESSON;
import static edu.bot.util.MessagesUtils.INVALID_DATE_FORMAT;
import static edu.bot.util.MessagesUtils.LESSON_OVERLAP;

@Log4j2
@RequiredArgsConstructor
public class RescheduleLessonCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (isRescheduleLessonCommand(command)) {
            return showLessonsForRescheduling(chatId);
        } else {
            return handleNewDateInput(command, chatId);
        }
    }

    private SendMessage showLessonsForRescheduling(long chatId) {
        List<Lesson> lessons = service.getLessons(chatId);
        if (lessons.isEmpty()) {
            return new SendMessage(chatId, "Нет доступных занятий для изменения.");
        }

        InlineKeyboardMarkup keyboard = KeyboardBuilder.createLessonKeyboardForReschedule(lessons);
        return new SendMessage(chatId, "Выберите занятие для изменения даты и времени:").replyMarkup(keyboard);
    }

    private SendMessage handleNewDateInput(String newDateString, long chatId) {
        UUID lessonId = service.getSelectedLesson(chatId);
        if (lessonId == null) {
            return new SendMessage(chatId, "Сначала выберите занятие для переноса.");
        }

        Date newDate = parseDate(newDateString);
        if (newDate == null) {
            return new SendMessage(chatId, INVALID_DATE_FORMAT);
        }

        if (service.isLessonOverlapping(chatId, newDate, service.getLessonDuration(chatId, lessonId))) {
            return new SendMessage(chatId, LESSON_OVERLAP);
        }

        service.rescheduleLesson(chatId, lessonId, newDate);
        service.setSelectedLesson(chatId, null);
        return new SendMessage(chatId, String.format("Занятие успешно перенесено на %s!", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(newDate)));
    }

    private boolean isRescheduleLessonCommand(String command) {
        return command.startsWith(RESCHEDULE_LESSON.getName());
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.parse(dateString);
        } catch (ParseException e) {
            log.warn("Invalid date format for lesson: " + dateString, e);
            return null;
        }
    }
}
