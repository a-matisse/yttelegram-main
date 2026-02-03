package cs.youtrade.telegram.buttons.menu;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public interface MenuStateInt<USER extends AbstractUserData, MENU_TYPE extends IMenuEnum, STATE extends Enum<STATE>> {
    List<InlineKeyboardRow> buildKeyboard();

    InlineKeyboardMarkup buildMarkup();

    MENU_TYPE getOption(String optionStr);

    MENU_TYPE[] getOptions();

    STATE executeCallback(TelegramClient bot, Update update, USER user, MENU_TYPE t);

    String getHeaderText(TelegramClient bot, USER user);
}
