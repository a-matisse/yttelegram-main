package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.message.DefaultMessageProcessor;
import cs.youtrade.telegram.buttons.def.message.DefaultOnErrorProcessor;
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

    public void sendDefErrMes(
            TelegramClient bot,
            USER user
    ) {
        getDefaultOnErrorProcessor().sendDefErrMes(bot, user);
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

    public DefaultMessageProcessor<USER, EDIT> getDefaultMessageProcessor(
            TelegramClient bot,
            Update update,
            USER user
    ) {
        return DefaultMessageProcessor.<USER, EDIT>defaultBuilder()
                .sender(sender)
                .bot(bot)
                .update(update)
                .user(user)
                .supportedEdit(supportedEdit())
                .build();
    }

    public DefaultOnErrorProcessor<USER> getDefaultOnErrorProcessor() {
        return DefaultOnErrorProcessor.<USER>errorBuilder()
                .sender(sender)
                .build();
    }
}
