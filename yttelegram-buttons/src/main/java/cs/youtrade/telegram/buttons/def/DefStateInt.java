package cs.youtrade.telegram.buttons.def;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface DefStateInt<USER extends AbstractUserData, STATE extends Enum<STATE>, MESSAGE> {
    /**
     * Какое состояние поддерживает эта команда (ключ для registry)
     */
    STATE supportedState();

    /**
     * Выполнить команду. Реализация получает Update и выгруженную сущность tgEntity
     */
    STATE execute(TelegramClient bot, Update update, USER user);

    /**
     * Сообщение, которое отправится пользователю при смене состояния
     */
    void executeOnState(TelegramClient bot, Update update, USER e);

    /**
     * Метод создания сообщения
     */
    MESSAGE buildMessage(TelegramClient bot, USER e);
}
