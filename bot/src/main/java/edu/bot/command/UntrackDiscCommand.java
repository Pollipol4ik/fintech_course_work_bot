package edu.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.service.CommandService;
import edu.bot.util.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class UntrackDiscCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isUntrackDiscCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Executing /untrack_disc command");

        var trackedFolders = service.getTrackedFolders(chatId);
        if (trackedFolders.isEmpty()) {
            return new SendMessage(chatId, "Нет папок для прекращения отслеживания.");
        }

        InlineKeyboardMarkup keyboard = KeyboardBuilder.createDiscUntrackKeyboard(trackedFolders);
        return new SendMessage(chatId, "Выберите папку для прекращения отслеживания:").replyMarkup(keyboard);
    }

    private boolean isUntrackDiscCommand(String command) {
        return command.startsWith(Command.UNTRACK_GOOGLE_DISC.getName());
    }
}
