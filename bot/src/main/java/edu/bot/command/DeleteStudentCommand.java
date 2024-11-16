package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Student;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static edu.bot.command.Command.DELETE_STUDENT;
import static edu.bot.util.MessagesUtils.NO_STUDENTS_FOUND;
import static edu.bot.util.MessagesUtils.CHOOSE_STUDENT_TO_DELETE;

@Log4j2
@RequiredArgsConstructor
public class DeleteStudentCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isDeleteStudentCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /delete_student command");
        return buildMessage(chatId);
    }

    private boolean isDeleteStudentCommand(String command) {
        return command.equals(DELETE_STUDENT.getName());
    }

    private SendMessage buildMessage(long chatId) {
        List<Student> students = service.getStudents(chatId);

        if (students.isEmpty()) {
            return new SendMessage(chatId, NO_STUDENTS_FOUND);
        }

        InlineKeyboardMarkup keyboard = KeyboardBuilder.createStudentDeleteKeyboard(students);
        return new SendMessage(chatId, CHOOSE_STUDENT_TO_DELETE).replyMarkup(keyboard);
    }
}
