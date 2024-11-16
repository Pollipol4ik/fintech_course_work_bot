package edu.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.dto.Lesson;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.UUID;

import static edu.bot.command.Command.MARK_PAYMENT;
import static edu.bot.util.MessagesUtils.SELECT_LESSON_FOR_PAYMENT;
import static edu.bot.util.MessagesUtils.NO_UNPAID_LESSONS;
import static edu.bot.util.MessagesUtils.PAYMENT_MARKED;

@Log4j2
@RequiredArgsConstructor
public class MarkPaymentCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isMarkPaymentCommand(command)) {
            return executeNext(command, chatId);
        }

        log.info("Executing /mark_payment command");

        if (commandHasLessonId(command)) {
            UUID lessonId = extractLessonId(command);
            return markPaymentForLesson(chatId, lessonId);
        }

        return buildMessage(chatId);
    }

    private SendMessage buildMessage(long chatId) {
        List<Lesson> unpaidLessons = service.getUnpaidLessons(chatId);

        if (unpaidLessons.isEmpty()) {
            return new SendMessage(chatId, NO_UNPAID_LESSONS);
        }

        var keyboard = KeyboardBuilder.createPaymentKeyboard(unpaidLessons);

        return new SendMessage(chatId, SELECT_LESSON_FOR_PAYMENT).replyMarkup(keyboard);
    }

    private boolean isMarkPaymentCommand(String command) {
        return command.startsWith(MARK_PAYMENT.getName());
    }

    private boolean commandHasLessonId(String command) {
        return command.split(" ").length > 1;
    }

    private UUID extractLessonId(String command) {
        String[] parts = command.split(" ");
        return UUID.fromString(parts[1]);
    }

    private SendMessage markPaymentForLesson(long chatId, UUID lessonId) {
        service.markPayment(chatId, lessonId);
        return new SendMessage(chatId, PAYMENT_MARKED);
    }
}

