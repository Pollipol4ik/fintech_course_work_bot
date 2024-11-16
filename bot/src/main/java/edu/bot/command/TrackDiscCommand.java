package edu.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class TrackDiscCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isTrackDiscCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /track_disc command");

        String[] splitCommand = command.split(" ", 2);
        if (splitCommand.length < 2) {
            return new SendMessage(chatId, "Необходимо указать ID папки для отслеживания.");
        }

        String folderId = splitCommand[1];
        service.trackGoogleDisc(chatId, folderId);
        return new SendMessage(chatId, "Папка с ID " + folderId + " добавлена для отслеживания.");
    }

    private boolean isTrackDiscCommand(String command) {
        return command.startsWith(Command.TRACK_GOOGLE_DISC.getName());
    }
}
