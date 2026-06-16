package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ISenderService {
    InputStream downloadFile(
            TelegramClient bot,
            Document doc
    );

    SendMessage createMessage(
            Long chatId,
            String text
    );

    void deleteMes(
            TelegramClient bot,
            Long chatId,
            Supplier<Integer> messageId,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            String text,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            MessageInfoDto messageInfo
    );
}
