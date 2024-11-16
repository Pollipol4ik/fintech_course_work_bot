package edu.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bot.message_sender.Sender;
import edu.bot.resolver.UpdateResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class BotUpdatesListener implements UpdatesListener {
    @Qualifier("sender")
    private final Sender messageSenderImpl;
    private final UpdateResolver updateResolver;

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            try {
                processUpdate(update);
            } catch (Exception e) {
                log.error("Error processing update: ", e);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        SendMessage message = updateResolver.resolve(update);
        messageSenderImpl.sendMessage(message);
    }
}
