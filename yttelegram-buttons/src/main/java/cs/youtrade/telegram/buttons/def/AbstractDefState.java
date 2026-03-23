package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractDefState<USER extends AbstractUserData, STATE extends Enum<STATE>, MESSAGE, EDIT>
        implements DefStateInt<USER, STATE, MESSAGE, EDIT> {
    private static final String DEFAULT_ERROR = "🚫 Server is not available. Try again in a couple of minutes...";
    protected final IMessageSender<USER, MESSAGE, EDIT> sender;

    public void sendDefErrMes(
            TelegramClient bot,
            USER user
    ) {
        sender.sendTextMes(bot, user, DEFAULT_ERROR, data ->
                user.setLastMessageId(data.getMessageId()));
    }

    @Override
    public void executeOnState(TelegramClient bot, Update update, USER user) {
        long lastMessageId = user.getLastMessageId();
        Class<?> mesSupportedEdit = user.getSupportedEdit();
        Class<?> supportedEdit = supportedEdit();
        // Editing the message if is available
        if (supportedEdit != null
                && !user.isUpdated()
                && supportedEdit.equals(mesSupportedEdit)
                && lastMessageId > 0
        ) {
            EDIT edit = buildEdit(bot, user);
            if (edit != null) {
                sender.sendEdit(bot, user, edit, getDefaultOnErrorProcessor());
                return;
            }
        }
        sendNewMessage(bot, update, user);
    }

    private void sendNewMessage(TelegramClient bot, Update update, USER user) {
        MESSAGE mes = buildMessage(bot, update, user);
        if (mes != null) {
            // And sending the new message
            sender.sendMessage(bot, user, mes, getDefaultMessageProcessor(bot, update, user));
        } else {
            sendDefErrMes(bot, user);
        }
    }

    public DefaultMessageProcessor getDefaultMessageProcessor(
            TelegramClient bot,
            Update update,
            USER user
    ) {
        return new DefaultMessageProcessor(bot, update, user);
    }

    public DefaultOnErrorProcessor getDefaultOnErrorProcessor() {
        return new DefaultOnErrorProcessor();
    }

    public class DefaultOnErrorProcessor implements MessageProcessor<USER> {
        @Override
        public void processError(TelegramClient bot, USER user) {
            sendDefErrMes(bot, user);
        }
    }

    @RequiredArgsConstructor
    public class DefaultMessageProcessor extends DefaultOnErrorProcessor {
        private final TelegramClient bot;
        private final Update update;
        private final USER user;

        @Override
        public void processMessage(MessageSentData data) {
            int prevMessageId = user.getLastMessageId();
            // Deleting the old message,
            // because there should be no menu duplicates (at least minimize them)
            if (prevMessageId <= 0) {
                if (update.hasCallbackQuery()) {
                    Integer callbackId = update.getCallbackQuery().getMessage().getMessageId();
                    if (callbackId != null)
                        prevMessageId = callbackId;
                }
            }
            if (prevMessageId > 0) {
                try {
                    sender.deleteMes(bot, user, prevMessageId, null);
                } catch (Exception e) {
                    log.error("Menu deletion aborted: {}", e.getMessage());
                }
            }
            user.setLastMessageId(data.getMessageId());
            // Setting the new mesSupportedEdit
            user.setSupportedEdit(supportedEdit());
            // Refreshing the update flag
            user.setUpdated(false);
        }
    }
}
