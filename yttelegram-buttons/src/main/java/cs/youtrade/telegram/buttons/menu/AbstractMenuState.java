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
public abstract class AbstractMenuState<
        USER extends AbstractUserData,
        MENU extends IMenuEnum,
        STATE extends Enum<STATE>,
        MESSAGE,
        EDIT
        > extends AbstractDefState<USER, STATE, MESSAGE, EDIT> implements MenuStateInt<USER, MENU, STATE> {
    public AbstractMenuState(
            IMessageSender<USER, MESSAGE, EDIT> sender
    ) {
        super(sender);
    }

    @Override
    public STATE execute(TelegramClient bot, Update update, USER user) {
        // execute side
        executeSide(bot, update, user);

        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();
            try {
                // Replies so the button will not be highlighted (if should be so before execution)
                if (!replyAfterExecution()) sender.replyCallback(bot, user, update, null);
                // Gets the next menu
                MENU menuType = getOption(callbackQuery, user);
                // Executes the callback
                var newState = executeCallback(bot, update, user, menuType);
                // Replies so the button will not be highlighted (if should be so after execution)
                if (replyAfterExecution()) sender.replyCallback(bot, user, update, null);
                // Returning the new state
                return newState;
            } catch (Exception e) {
                log.error("Callback error", e);
                // fallback message
                sendDefErrMes(bot, user);
                // returning the error state
                return errorType(user);
            }
        }

        return supportedState();
    }

    public STATE errorType(USER userData) {
        return supportedState();
    }

    @Override
    public List<InlineKeyboardRow> buildKeyboard(USER user) {
        return Arrays
                .stream(getOptions(user))
                .collect(Collectors.groupingBy(IMenuEnum::getRowNum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        generateRow(entry.getValue(), user))
                .filter(Objects::nonNull)
                .toList();
    }

    private InlineKeyboardRow generateRow(List<MENU> buttons, USER user) {
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

    private InlineKeyboardButton generateButton(MENU menuOption, USER user) {
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
            builder.callbackData(menuOption.getOptionName());
        // Setting the button style for button if needed
        var buttonStyle = getButtonStyle(user).get(menuOption);
        if (buttonStyle != null) {
            var style = buttonStyle.apply(user);
            if (style != null && style != InlineKeyboardButtonStyle.DEFAULT)
                builder.style(buttonStyle.apply(user).getStyle());
        }
        // Building the button
        return builder
                .pay(menuOption.isPay())
                .build();
    }

    public MENU getOption(String callbackQuery, USER user) {
        for (MENU menu : getOptions(user))
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

    public Map<MENU, String> getUrls(USER user) {
        return Map.of();
    }

    public Map<MENU, Predicate<USER>> getVisibilityPredicates(USER user) {
        return Map.of();
    }

    public Map<MENU, Function<USER, String>> getTextFunctions(USER user) {
        return Map.of();
    }

    public Map<MENU, Function<USER, InlineKeyboardButtonStyle>> getButtonStyle(USER user) {
        return Map.of();
    }
}
