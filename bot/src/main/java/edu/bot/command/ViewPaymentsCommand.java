package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;

import static edu.bot.command.Command.VIEW_PAYMENTS;
import static edu.bot.util.MessagesUtils.NO_PAYMENTS;

@Log4j2
@RequiredArgsConstructor
public class ViewPaymentsCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isViewPaymentsCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /view_payments command");
        return buildMessage(chatId);
    }

    private boolean isViewPaymentsCommand(String command) {
        return command.equals(VIEW_PAYMENTS.getName());
    }

    private SendMessage buildMessage(long chatId) {
        var lastPayments = service.getLastFivePayments(chatId);

        if (lastPayments.isEmpty()) {
            return new SendMessage(chatId, NO_PAYMENTS);
        }
        String paymentsList = lastPayments.stream()
                .map(payment -> {
                    var lesson = service.getLessons(chatId).stream()
                            .filter(l -> l.getId().equals(payment.getLessonId()))
                            .findFirst().orElse(null);
                    if (lesson != null) {
                        double amount = lesson.getPrice();
                        return "Оплата на сумму: " + amount + ", Дата: " + payment.getPaymentDate();
                    }
                    return "Оплата не привязана к занятию";
                })
                .collect(Collectors.joining("\n"));

        InlineKeyboardMarkup keyboard = KeyboardBuilder.createPaymentKeyboard(service.getLessons(chatId));

        return new SendMessage(chatId, paymentsList)
                .replyMarkup(keyboard);
    }
}
