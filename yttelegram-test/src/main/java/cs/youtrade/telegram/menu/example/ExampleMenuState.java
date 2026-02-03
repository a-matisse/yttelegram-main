package cs.youtrade.telegram.menu.example;

import cs.youtrade.telegram.buttons.menu.text.AbstractTextMenuState;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import cs.youtrade.telegram.menu.ExampleState;
import cs.youtrade.telegram.prototype.UserData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class ExampleMenuState extends AbstractTextMenuState<UserData, ExampleMenu, ExampleState> {
    public ExampleMenuState(
            IMessageSender<UserData, SendMessage> sender
    ) {
        super(sender);
    }

    @Override
    public ExampleState supportedState() {
        return ExampleState.MAIN;
    }

    @Override
    public ExampleMenu getOption(String optionStr) {
        return ExampleMenu.valueOf(optionStr);
    }

    @Override
    public ExampleMenu[] getOptions() {
        return ExampleMenu.values();
    }

    @Override
    public ExampleState executeCallback(TelegramClient bot, Update update, UserData userData, ExampleMenu t) {
        return switch (t) {
            case BUTTON_1 -> ExampleState.BUTTON_1;
            case BUTTON_2 -> ExampleState.BUTTON_2;
            case BUTTON_3 -> ExampleState.BUTTON_3;
        };
    }

    @Override
    public String getHeaderText(TelegramClient bot, UserData userData) {
        return "Main Menu";
    }
}
