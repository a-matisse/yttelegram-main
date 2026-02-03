package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractDefState<USER extends AbstractUserData, STATE extends Enum<STATE>, MESSAGE>
        implements DefStateInt<USER, STATE, MESSAGE> {
    protected static final String SERVER_ERROR_MES = "🚫 Сервер временно недоступен. Попробуйте через несколько минут.";
    protected final IMessageSender<USER, MESSAGE> sender;

    @Override
    public void executeOnState(TelegramClient bot, Update update, USER user) {
        sender.sendMessage(bot, user, buildMessage(bot, user));
    }

    public void sendDefErrMes(TelegramClient bot, long chatId) {
        sender.sendDefErrMes(bot, chatId);
    }
}
