package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Lesson;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static edu.bot.util.MessagesUtils.NO_TRACKED_LESSON;
import static edu.bot.util.MessagesUtils.TRACKED_LESSON;

@Log4j2
@RequiredArgsConstructor
public class ListLessonsCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isListLessonsCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Command /list_lessons has been executed");
        return buildMessage(chatId);
    }

    private boolean isListLessonsCommand(String command) {
        return command.equals(Command.LIST_LESSONS.getName());
    }

    private SendMessage buildMessage(long chatId) {
        List<Lesson> lessons = service.getLessons(chatId);
        if (lessons.isEmpty()) {
            return buildNoTrackedLessonsMessage(chatId);
        }
        InlineKeyboardMarkup keyboard = KeyboardBuilder.createLessonKeyboard(lessons);
        return buildTrackedLessonsMessage(chatId, keyboard);
    }

    private SendMessage buildNoTrackedLessonsMessage(long chatId) {
        return new SendMessage(chatId, NO_TRACKED_LESSON);
    }

    private SendMessage buildTrackedLessonsMessage(long chatId, InlineKeyboardMarkup keyboard) {
        return new SendMessage(chatId, TRACKED_LESSON).replyMarkup(keyboard);
    }
}
