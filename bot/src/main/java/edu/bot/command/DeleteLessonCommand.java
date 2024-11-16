package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Lesson;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static edu.bot.command.Command.DELETE_LESSON;
import static edu.bot.util.MessagesUtils.CHOOSE_LESSON_TO_DELETE;
import static edu.bot.util.MessagesUtils.NO_LESSONS_FOUND;

@Log4j2
@RequiredArgsConstructor
public class DeleteLessonCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isDeleteLessonCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /delete_lesson command");
        return buildMessage(chatId);
    }

    private boolean isDeleteLessonCommand(String command) {
        return command.equals(DELETE_LESSON.getName());
    }

    private SendMessage buildMessage(long chatId) {
        List<Lesson> lessons = service.getLessons(chatId);

        if (lessons.isEmpty()) {
            return new SendMessage(chatId, NO_LESSONS_FOUND);
        }

        InlineKeyboardMarkup keyboard = KeyboardBuilder.createLessonDeleteKeyboard(lessons);
        return new SendMessage(chatId, CHOOSE_LESSON_TO_DELETE).replyMarkup(keyboard);
    }
}
