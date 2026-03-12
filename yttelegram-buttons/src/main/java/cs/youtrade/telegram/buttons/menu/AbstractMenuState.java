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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
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
    public List<InlineKeyboardRow> buildKeyboard(USER user) {
        return Arrays
                .stream(getOptions())
                .collect(Collectors.groupingBy(IMenuEnum::getRowNum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        generateRow(entry.getValue(), user))
                .filter(Objects::nonNull)
                .toList();
    }

    private InlineKeyboardRow generateRow(List<MENU_TYPE> buttons, USER user) {
        var buttonList = buttons
                .stream()
                .map(menu ->
                        generateButton(menu, user))
                .filter(Objects::nonNull)
                .toList();
        return !buttonList.isEmpty()
                ? new InlineKeyboardRow(buttonList)
                : null;
    }

    private InlineKeyboardButton generateButton(MENU_TYPE menuOption, USER user) {
        // Checking the ability to create the button
        var visibility = getVisibilityPredicates(user).get(menuOption);
        if (visibility != null && !visibility.test(user))
            return null;
        // Initializing the factory
        InlineKeyboardButton.InlineKeyboardButtonBuilder<?, ?> builder = InlineKeyboardButton.builder();
        // Setting the button name if there is any function
        var textFunction = getTextFunctions(user).get(menuOption);
        if (textFunction != null)
            builder.text(textFunction.apply(user));
        else
            builder.text(menuOption.getButtonName());
        // Setting the url for button if there is any
        // and button should redirect to url
        var url = getUrls(user).get(menuOption);
        if (url != null)
            builder.url(url);
        else
            builder.callbackData(menuOption.toString());
        // Building the button
        return builder.build();
    }

    public MENU_TYPE getOption(String callbackQuery) {
        for (MENU_TYPE menu : getOptions())
            if (menu.getOptionName().equals(callbackQuery))
                return menu;
        throw new IllegalArgumentException("Unknown menu option: " + callbackQuery);
    }

    @Override
    public InlineKeyboardMarkup buildMarkup(USER user) {
        return InlineKeyboardMarkup.builder()
                .keyboard(buildKeyboard(user))
                .build();
    }

    public void executeSide(TelegramClient bot, Update update, USER userData) {
    }

    public Map<MENU_TYPE, String> getUrls(USER user) {
        return Map.of();
    }

    public Map<MENU_TYPE, Predicate<USER>> getVisibilityPredicates(USER user) {
        return Map.of();
    }

    public Map<MENU_TYPE, Function<USER, String>> getTextFunctions(USER user) {
        return Map.of();
    }
}
