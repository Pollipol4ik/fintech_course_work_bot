package edu.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.command.CommandChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class UpdateMessageResolver extends UpdateResolver {

    private final CommandChain commandChain;

    @Override
    public SendMessage resolve(Update update) {
        if (isUpdateWithoutMessage(update)) {
            log.warn("Update without message or text, passing to next resolver.");
            return resolveNext(update);
        }
        return executeCommandForMessage(update);
    }

    private boolean isUpdateWithoutMessage(Update update) {
        return update.message() == null || update.message().text() == null;
    }

    private SendMessage executeCommandForMessage(Update update) {
        String messageText = update.message().text();
        long chatId = update.message().chat().id();
        log.info("Processing message command: {}", messageText);
        return commandChain.executeCommand(messageText, chatId);
    }
}
