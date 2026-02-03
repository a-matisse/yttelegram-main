package cs.youtrade.telegram.prototype;

import cs.youtrade.telegram.buttons.sender.ISenderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;

@Service
@Log4j2
public class TelegramSendMessageService implements ISenderService {
    @Override
    public InputStream downloadFile(TelegramClient bot, Document doc) {
        try {
            GetFile gf = new GetFile(doc.getFileId());
            var tgFile = bot.execute(gf);
            return bot.downloadFileAsStream(tgFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SendMessage createMessage(Long chatId, String text) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    @Override
    public void deleteMes(TelegramClient bot, Long chatId, Update update) {
        try {
            int messageId = update.getMessage().getMessageId();
            DeleteMessage delete = DeleteMessage
                    .builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .build();
            bot.execute(delete);
        } catch (TelegramApiException e) {
            log.error("Couldn't delete message", e);
        }
    }

    @Override
    public void deleteCallback(TelegramClient bot, Long chatId, Update update) {
        try {
            int callbackId = update.getCallbackQuery().getMessage().getMessageId();
            DeleteMessage delete = DeleteMessage
                    .builder()
                    .chatId(chatId)
                    .messageId(callbackId)
                    .build();
            bot.execute(delete);
        } catch (TelegramApiException e) {
            log.error("Couldn't delete callback", e);
        }
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, String text) {
        try {
            var message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Couldn't send message", e);
        }
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Couldn't send message", e);
        }
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, SendDocument doc) {
        try {
            bot.execute(doc);
        } catch (TelegramApiException e) {
            log.error("Couldn't send message", e);
        }
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, EditMessageReplyMarkup edit) {
        try {
            bot.execute(edit);
        } catch (TelegramApiException e) {
            log.error("Couldn't send message", e);
        }
    }

    @Override
    public void sendMessage(TelegramClient bot, Long chatId, AnswerCallbackQuery ack) {
        try {
            bot.execute(ack);
        } catch (TelegramApiException e) {
            log.error("Couldn't send message", e);
        }
    }
}
