package cs.youtrade.telegram.buttons.def.message;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@SuperBuilder(toBuilder = true, builderMethodName = "errorBuilder")
public class DefaultOnErrorProcessor<USER extends AbstractUserData> implements MessageProcessor<USER> {
    private static final String DEFAULT_ERROR = "🚫 Server is not available. Try again in a couple of minutes...";

    @NonNull
    protected IMessageSender<USER, ?, ?> sender;

    public void sendDefErrMes(
            TelegramClient bot,
            USER user
    ) {
        sender.sendTextMes(bot, user, DEFAULT_ERROR, data ->
                user.setLastMessageId(data.getMessageId()));
    }

    @Override
    public void processError(TelegramClient bot, USER user) {
        sendDefErrMes(bot, user);
    }
}
