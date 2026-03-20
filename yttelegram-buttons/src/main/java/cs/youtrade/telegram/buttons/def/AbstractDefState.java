package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractDefState<USER extends AbstractUserData, STATE extends Enum<STATE>, MESSAGE, EDIT>
        implements DefStateInt<USER, STATE, MESSAGE, EDIT> {
    protected final IMessageSender<USER, MESSAGE, EDIT> sender;

    public void sendDefErrMes(TelegramClient bot, USER user) {
        sender.sendDefErrMes(bot, user);
    }

    @Override
    public void executeOnState(TelegramClient bot, Update update, USER user) {
        EDIT edit = buildEdit(bot, user);
        long lastMessageId = user.getLastMessageId();
        Class<?> mesSupportedEdit = user.getSupportedEdit();
        Class<?> supportedEdit = supportedEdit();
        // Editing the message if is available
        if (supportedEdit != null
                && edit != null
                && !user.isUpdated()
                && supportedEdit.equals(mesSupportedEdit)
                && lastMessageId > 0
        ) {
            sender.sendEdit(bot, user, edit, null);
        } else {
            sendNewMessage(bot, user, supportedEdit);
        }
    }

    private void sendNewMessage(TelegramClient bot, USER user, Class<?> supportedEdit) {
        MESSAGE mes = buildMessage(bot, user);
        if (mes != null) {
            // And sending the new message
            sender.sendMessage(bot, user, mes, data -> {
                int prevMessageId = user.getLastMessageId();
                // Deleting the old message,
                // because there should be no menu duplicates (at least minimize them)
                if (prevMessageId > 0) {
                    try {
                        sender.deleteMes(bot, user, prevMessageId, null);
                    } catch (Exception e) {
                        log.error("Menu deletion aborted: {}", e.getMessage());
                    }
                }
                user.setLastMessageId(data.getMessageId());
                // Setting the new mesSupportedEdit
                user.setSupportedEdit(supportedEdit);
            });
        } else {
            sendDefErrMes(bot, user);
        }
    }
}
