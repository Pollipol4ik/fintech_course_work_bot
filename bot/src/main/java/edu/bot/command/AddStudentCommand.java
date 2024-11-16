package edu.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Student;
import edu.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

import static edu.bot.command.Command.ADD_STUDENT;
import static edu.bot.util.MessagesUtils.STUDENT_ADDED;

@Log4j2
@RequiredArgsConstructor
public class AddStudentCommand extends CommandExecutor {
    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!command.startsWith(ADD_STUDENT.getName())) {
            return executeNext(command, chatId);
        }

        String[] splitCommand = command.split(" ", 5);
        if (splitCommand.length < 5) {
            return new SendMessage(chatId, "Пример: /add_student Имя Фамилия Отчество Контакт");
        }

        String firstName = splitCommand[1];
        String lastName = splitCommand[2];
        String middleName = splitCommand[3];
        String contactInfo = splitCommand[4];

        if (service.isStudentExists(chatId, firstName, lastName, middleName)) {
            return new SendMessage(chatId, "Студент с таким ФИО уже существует.");
        }

        Student student = new Student(UUID.randomUUID(), firstName, lastName, middleName, contactInfo);
        service.addStudent(chatId, student);
        return new SendMessage(chatId, STUDENT_ADDED);
    }
}
