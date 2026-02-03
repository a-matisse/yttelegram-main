package cs.youtrade.telegram.prototype;

import cs.youtrade.telegram.buttons.sender.BaseMessageSender;
import cs.youtrade.telegram.buttons.sender.ISenderService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class TextMessageSender extends BaseMessageSender<UserData, SendMessage> {
    public TextMessageSender(ISenderService sender) {
        super(sender);
    }

    @Override
    public void sendMessage(TelegramClient bot, UserData user, SendMessage mes) {
        long chatId = user.getChatId();
        if (mes == null)
            sendDefErrMes(bot, chatId);
        else
            sender.sendMessage(bot, chatId, mes);
    }
}
