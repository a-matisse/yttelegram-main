package cs.youtrade.telegram.menu.example.button2;

import cs.youtrade.telegram.buttons.sender.IMessageSender;
import cs.youtrade.telegram.menu.ExampleState;
import cs.youtrade.telegram.prototype.UserData;
import cs.youtrade.telegram.prototype.text.BaseTerminalTextMenuState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class Button2State extends BaseTerminalTextMenuState {
    public Button2State(
            IMessageSender<UserData, SendMessage> sender
    ) {
        super(sender);
    }

    @Override
    public ExampleState returnState() {
        return ExampleState.MAIN;
    }

    @Override
    public ExampleState supportedState() {
        return ExampleState.BUTTON_2;
    }

    @Override
    public String getHeaderText(TelegramClient bot, UserData userData) {
        return "Welcome to the second menu";
    }
}
