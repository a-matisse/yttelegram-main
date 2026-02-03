package cs.youtrade.telegram.buttons.sender;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;

public interface ISenderService {
    InputStream downloadFile(TelegramClient bot, Document doc);
    SendMessage createMessage(Long chatId, String text);
    void deleteMes(TelegramClient bot, Long chatId, Update update);
    void deleteCallback(TelegramClient bot, Long chatId, Update update);
    void sendMessage(TelegramClient bot, Long chatId, String text);
    void sendMessage(TelegramClient bot, Long chatId, SendMessage message);
    void sendMessage(TelegramClient bot, Long chatId, SendDocument doc);
    void sendMessage(TelegramClient bot, Long chatId, EditMessageReplyMarkup edit);
    void sendMessage(TelegramClient bot, Long chatId, AnswerCallbackQuery ack);
}
