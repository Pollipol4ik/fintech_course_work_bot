package edu.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ListDiscCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isListDiscCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /list_google_disc command");

        List<String> trackedFolders = service.getTrackedFolders(chatId);
        if (trackedFolders.isEmpty()) {
            return new SendMessage(chatId, "Нет отслеживаемых папок.");
        }

        String foldersList = String.join("\n", trackedFolders);
        return new SendMessage(chatId, "Отслеживаемые папки:\n" + foldersList);
    }

    private boolean isListDiscCommand(String command) {
        return command.startsWith(Command.LIST.getName());
    }
}
