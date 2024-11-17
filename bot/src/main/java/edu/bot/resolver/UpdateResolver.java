package edu.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class UpdateResolver {

    private UpdateResolver next;

    public static UpdateResolver link(UpdateResolver first, UpdateResolver... chain) {
        UpdateResolver head = first;
        for (UpdateResolver resolver : chain) {
            head.next = resolver;
            head = resolver;
        }
        return first;
    }

    public abstract SendMessage resolve(Update update);

    protected SendMessage resolveNext(Update update) {
        if (next == null) {
            log.error("No handler found for this update type");
            throw new RuntimeException("Invalid update: No handler found for this update type");
        }
        return next.resolve(update);
    }
}
