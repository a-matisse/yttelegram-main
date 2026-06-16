package cs.youtrade.telegram.buttons.sender;

import cs.youtrade.telegram.buttons.util.MessageSentData;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
public class MessageInfoDto {
    private final MessageType messageType;
    private final TelegramClient bot;
    private final long chatId;
    private final Consumer<MessageSentData> onMessage;
    private final Supplier<?> messageSupplier;

    private MessageInfoDto(
            TelegramClient bot,
            long chatId,
            MessageType messageType,
            Supplier<?> messageSupplier,
            Consumer<MessageSentData> onMessage
    ) {
        this.bot = bot;
        this.chatId = chatId;
        this.messageType = messageType;
        this.messageSupplier = messageSupplier;
        this.onMessage = onMessage;
    }

    public static MessageInfoDto text(
            TelegramClient bot,
            long chatId,
            Supplier<SendMessage> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.TEXT, supplier, onMessage);
    }

    public static MessageInfoDto doc(
            TelegramClient bot,
            long chatId,
            Supplier<SendDocument> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.DOCUMENT, supplier, onMessage);
    }

    public static MessageInfoDto photo(
            TelegramClient bot,
            long chatId,
            Supplier<SendPhoto> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.PHOTO, supplier, onMessage);
    }

    public static MessageInfoDto invoice(
            TelegramClient bot,
            long chatId,
            Supplier<SendInvoice> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.INVOICE, supplier, onMessage);
    }

    public static MessageInfoDto editText(
            TelegramClient bot,
            long chatId,
            Supplier<EditMessageText> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.EDIT_TEXT, supplier, onMessage);
    }

    public static MessageInfoDto editMedia(
            TelegramClient bot,
            long chatId,
            Supplier<EditMessageMedia> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.EDIT_MEDIA, supplier, onMessage);
    }

    public static MessageInfoDto editMarkup(
            TelegramClient bot,
            long chatId,
            Supplier<EditMessageReplyMarkup> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.EDIT_MARKUP, supplier, onMessage);
    }

    public static MessageInfoDto ack(
            TelegramClient bot,
            long chatId,
            Supplier<AnswerCallbackQuery> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.ANSWER_CALLBACK, supplier, onMessage);
    }

    public static MessageInfoDto apcq(
            TelegramClient bot,
            long chatId,
            Supplier<AnswerPreCheckoutQuery> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.ANSWER_PRE_CHECKOUT, supplier, onMessage);
    }

    public static MessageInfoDto delete(
            TelegramClient bot,
            long chatId,
            Supplier<DeleteMessage> supplier,
            Consumer<MessageSentData> onMessage
    ) {
        return new MessageInfoDto(bot, chatId, MessageType.DELETE, supplier, onMessage);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMessage() {
        return (T) messageSupplier.get();
    }
}
