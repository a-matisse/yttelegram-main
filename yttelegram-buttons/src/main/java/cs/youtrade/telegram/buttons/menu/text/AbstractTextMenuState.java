package cs.youtrade.telegram.buttons.menu.text;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.WebPagePreviewable;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.menu.AbstractMenuState;
import cs.youtrade.telegram.buttons.sender.text.BaseTextMessageSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class AbstractTextMenuState<USER extends AbstractUserData, MENU_TYPE extends IMenuEnum, STATE extends Enum<STATE>>
        extends AbstractMenuState<USER, MENU_TYPE, STATE, SendMessage, EditMessageText> implements WebPagePreviewable {
    public AbstractTextMenuState(
            BaseTextMessageSender<USER> sender
    ) {
        super(sender);
    }

    public SendMessage buildMessage(TelegramClient bot, Update update, USER user) {
        String ans = "";
        try {
            String header = getHeaderText(bot, user);
            if (header != null)
                ans = header;
        } catch (Exception ignored) {
        }
        if (ans.isEmpty())
            ans = "Не удалось обработать сообщение";

        return SendMessage
                .builder()
                .chatId(user.getChatId())
                .text(ans)
                .replyMarkup(buildMarkup(user))
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(disableWebPagePreview())
                .build();
    }

    @Override
    public EditMessageText buildEdit(TelegramClient bot, USER user) {
        String ans = "";
        try {
            String header = getHeaderText(bot, user);
            if (header != null)
                ans = header;
        } catch (Exception ignored) {
        }
        if (ans.isEmpty())
            ans = "Не удалось обработать сообщение";

        return EditMessageText
                .builder()
                .chatId(user.getChatId())
                .messageId(user.getLastMessageId())
                .text(ans)
                .replyMarkup(buildMarkup(user))
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(disableWebPagePreview())
                .build();
    }

    @Override
    public Class<EditMessageText> supportedEdit() {
        return EditMessageText.class;
    }
}
