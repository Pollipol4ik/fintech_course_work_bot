package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Student;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static edu.bot.util.MessagesUtils.NO_STUDENTS_FOUND;
import static edu.bot.util.MessagesUtils.STUDENT_LIST;

@Log4j2
@RequiredArgsConstructor
public class ListStudentsCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isListStudentsCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Command /list_students has been executed");
        return buildMessage(chatId);
    }

    private boolean isListStudentsCommand(String command) {
        return command.equals(Command.VIEW_STUDENT_LIST.getName());
    }

    private SendMessage buildMessage(long chatId) {
        List<Student> students = service.getStudents(chatId);
        if (students.isEmpty()) {
            return new SendMessage(chatId, NO_STUDENTS_FOUND);
        }
        InlineKeyboardMarkup keyboard = KeyboardBuilder.createStudentKeyboard(students);
        return new SendMessage(chatId, STUDENT_LIST).replyMarkup(keyboard);
    }
}
