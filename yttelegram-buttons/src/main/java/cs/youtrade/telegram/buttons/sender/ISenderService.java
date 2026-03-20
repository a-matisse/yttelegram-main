package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.util.MessageSentData;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;
import java.util.function.Consumer;

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
            int messageId,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            String text,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            SendMessage message,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            SendDocument doc,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            SendPhoto sendPhoto,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            SendInvoice invoice,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            EditMessageText edit,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            EditMessageMedia edit,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            EditMessageReplyMarkup edit,
            Consumer<MessageSentData> onMessage
    );

    void sendMessage(
            TelegramClient bot,
            Long chatId,
            AnswerCallbackQuery ack,
            Consumer<MessageSentData> onMessage
    );
}
