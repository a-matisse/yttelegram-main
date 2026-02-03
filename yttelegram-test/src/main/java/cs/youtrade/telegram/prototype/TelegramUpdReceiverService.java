package cs.youtrade.telegram.prototype;

import cs.youtrade.telegram.buttons.menu.BaseMenuRegistry;
import cs.youtrade.telegram.menu.ExampleState;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class TelegramUpdReceiverService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient bot;
    private final String botToken;
    private final TelegramSendMessageService sender;
    private final BaseMenuRegistry<UserData, ExampleState> stateRegistry;
    private final ConcurrentHashMap<Long, ExampleState> userMap = new ConcurrentHashMap<>();

    @Autowired
    public TelegramUpdReceiverService(
            @Value("${tg.token}") String botToken,
            TelegramSendMessageService sender,
            BaseMenuRegistry<UserData, ExampleState> stateRegistry
    ) {
        this.bot = new OkHttpTelegramClient(botToken);
        this.botToken = botToken;
        this.sender = sender;
        this.stateRegistry = stateRegistry;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() && !update.hasCallbackQuery())
            return;

        // 1. Поиск пользователя в системе
        Long chatId = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();
        UserData user = new UserData(chatId);

        procedeTask(user, update);
    }

    private void procedeTask(UserData user, Update update) {
        // 1. Getting chatId
        long chatId = user.getChatId();
        ExampleState state = userMap.computeIfAbsent(chatId, id -> ExampleState.MAIN);

        // 2. Command procedure
        ExampleState newState = stateRegistry.get(state).execute(bot, update, user);

        // 3. Saving the new state
        userMap.put(chatId, newState);

        // 4. Sending the message if the state has changed
        if (state != newState)
            stateRegistry.get(newState).executeOnState(bot, update, user);

        // 5. Deleting last menu to not flood with messages
        if (update.hasCallbackQuery())
            sender.deleteCallback(bot, user.getChatId(), update);
    }
}
