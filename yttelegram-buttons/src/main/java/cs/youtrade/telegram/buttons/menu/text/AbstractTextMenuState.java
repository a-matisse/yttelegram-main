package cs.youtrade.telegram.buttons.menu.text;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.menu.AbstractMenuState;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class AbstractTextMenuState<USER extends AbstractUserData, MENU_TYPE extends IMenuEnum, STATE extends Enum<STATE>>
        extends AbstractMenuState<USER, MENU_TYPE, STATE, SendMessage> {
    public AbstractTextMenuState(
            IMessageSender<USER, SendMessage> sender
    ) {
        super(sender);
    }

    public SendMessage buildMessage(TelegramClient bot, USER userData) {
        String ans = "";
        try {
            String header = getHeaderText(bot, userData);
            if (header != null)
                ans = header;
        } catch (Exception ignored) {
        }
        if (ans.isEmpty())
            ans = "Не удалось обработать сообщение";

        return SendMessage
                .builder()
                .text(ans)
                .chatId(userData.getChatId())
                .replyMarkup(buildMarkup())
                .parseMode(ParseMode.HTML)
                .build();
    }
}
