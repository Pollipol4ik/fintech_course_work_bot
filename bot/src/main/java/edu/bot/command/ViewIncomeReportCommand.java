package edu.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static edu.bot.command.Command.VIEW_INCOME_REPORT;
import static edu.bot.util.MessagesUtils.NO_INCOME_DATA;

@Log4j2
@RequiredArgsConstructor
public class ViewIncomeReportCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isViewIncomeReportCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /income_report command");

        String[] splitCommand = command.split(" ");
        if (splitCommand.length < 3) {
            return new SendMessage(chatId, "Пример: /income_report Месяц Год");
        }

        int month;
        int year;
        try {
            month = Integer.parseInt(splitCommand[1]);
            year = Integer.parseInt(splitCommand[2]);
        } catch (NumberFormatException e) {
            return new SendMessage(chatId, "Неверный формат месяца или года.");
        }

        String report = service.getIncomeReport(chatId, month, year);
        return new SendMessage(chatId, report.isEmpty() ? NO_INCOME_DATA : report);
    }

    private boolean isViewIncomeReportCommand(String command) {
        return command.startsWith(VIEW_INCOME_REPORT.getName());
    }
}
