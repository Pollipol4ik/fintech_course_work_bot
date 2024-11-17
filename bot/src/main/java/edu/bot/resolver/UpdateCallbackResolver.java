package edu.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

import static edu.bot.util.MessagesUtils.FORMAT_ERROR;
import static edu.bot.util.MessagesUtils.LESSON_DELETED;
import static edu.bot.util.MessagesUtils.STUDENT_DELETED;

@RequiredArgsConstructor
@Log4j2
public class UpdateCallbackResolver extends UpdateResolver {

    private final CommandService commandService;

    @Override
    public SendMessage resolve(Update update) {
        if (update.callbackQuery() == null) {
            return resolveNext(update);
        }

        long userId = update.callbackQuery().from().id();
        String callbackData = update.callbackQuery().data();
        log.info("Received callback query data: {}", callbackData);

        try {
            return processCallback(userId, callbackData);
        } catch (IllegalArgumentException e) {
            log.error("Error processing callback: {}", e.getMessage());
            return new SendMessage(userId, FORMAT_ERROR);
        }
    }

    private SendMessage processCallback(long userId, String callbackData) {
        if (!isValidCallbackData(callbackData)) {
            throw new IllegalArgumentException("Invalid callback data format");
        }

        String[] parts = callbackData.split(":");
        String action = parts[0];
        String entityId = parts.length > 1 ? parts[1] : null;

        switch (action) {
            case "/delete_lesson":
                commandService.deleteLesson(userId, UUID.fromString(entityId));
                return new SendMessage(userId, LESSON_DELETED);

            case "/delete_student":
                commandService.deleteStudent(userId, UUID.fromString(entityId));
                return new SendMessage(userId, STUDENT_DELETED);

            case "/reschedule_lesson":
                commandService.setSelectedLesson(userId, UUID.fromString(entityId));
                // Prompt the user to enter a new date and time
                return new SendMessage(userId, "На какую дату и время хотите перенести занятие? Используйте формат: yyyy-MM-dd HH:mm");
            case "/mark_payment":
                commandService.markPayment(userId, UUID.fromString(entityId));
                return new SendMessage(userId, "Оплата за занятие успешно отмечена!");

            case "/view_payments":
                String paymentsList = commandService.getPaymentsList(userId);
                return new SendMessage(userId, paymentsList.isEmpty() ? "Нет записей об оплатах." : paymentsList);

            case "/income_report":
                commandService.setAwaitingIncomeReport(userId);
                return new SendMessage(userId, "Введите месяц и год для отчета о доходах в формате MM-yyyy:");

            case "/untrack_disc":
                commandService.untrackGoogleDisc(userId, entityId);
                return new SendMessage(userId, "Отслеживание ссылки прекращено: " + entityId);

            default:
                throw new IllegalArgumentException("Unexpected action in callback data");
        }
    }

    private boolean isValidCallbackData(String data) {
        String[] parts = data.split(":");
        return parts.length == 2 && isValidUUID(parts[1]) && (
                "/delete_lesson".equals(parts[0]) ||
                        "/delete_student".equals(parts[0]) ||
                        "/reschedule_lesson".equals(parts[0]) ||
                        "/mark_payment".equals(parts[0]) ||
                        "/view_payments".equals(parts[0]) ||
                        "/income_report".equals(parts[0]) ||
                        "/untrack_disc".equals(parts[0])
        );
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format in callback data: {}", uuid);
            return false;
        }
    }

}
