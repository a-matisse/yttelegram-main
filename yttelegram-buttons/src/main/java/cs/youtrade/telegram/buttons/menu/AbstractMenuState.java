package cs.youtrade.telegram.buttons.menu;

import cs.youtrade.telegram.buttons.IMenuEnum;
import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.AbstractDefState;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public abstract class AbstractMenuState<USER extends AbstractUserData, MENU_TYPE extends IMenuEnum, STATE extends Enum<STATE>, MESSAGE>
        extends AbstractDefState<USER, STATE, MESSAGE>
        implements MenuStateInt<USER, MENU_TYPE, STATE> {
    public AbstractMenuState(
            IMessageSender<USER, MESSAGE> sender
    ) {
        super(sender);
    }

    @Override
    public STATE execute(TelegramClient bot, Update update, USER userData) {
        // execute side
        executeSide(bot, update, userData);

        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();
            try {
                sender.replyCallback(bot, userData, update);
                MENU_TYPE menuType = getOption(callbackQuery);
                return executeCallback(bot, update, userData, menuType);
            } catch (Exception e) {
                log.error("Callback error", e);
            }
        }

        if (update.hasMessage() && update.getMessage().hasText())
            sender.sendMessage(bot, userData, buildMessage(bot, userData));

        return supportedState();
    }

    @Override
    public List<InlineKeyboardRow> buildKeyboard() {
        return Arrays
                .stream(getOptions())
                .collect(Collectors.groupingBy(IMenuEnum::getRowNum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new InlineKeyboardRow(entry
                        .getValue()
                        .stream()
                        .map(menuOption ->
                                InlineKeyboardButton
                                        .builder()
                                        .text(menuOption.getButtonName())
                                        .callbackData(menuOption.toString())
                                        .build()
                        )
                        .toList()
                ))
                .toList();
    }

    @Override
    public InlineKeyboardMarkup buildMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboard(buildKeyboard())
                .build();
    }

    public void executeSide(TelegramClient bot, Update update, USER userData) {
    }
}
