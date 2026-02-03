package cs.youtrade.telegram.prototype.text;

import cs.youtrade.telegram.buttons.menu.text.AbstractTextMenuState;
import cs.youtrade.telegram.buttons.sender.IMessageSender;
import cs.youtrade.telegram.menu.ExampleState;
import cs.youtrade.telegram.prototype.UserData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public abstract class BaseTerminalTextMenuState extends AbstractTextMenuState<UserData, TerminalMenu, ExampleState> {
    public BaseTerminalTextMenuState(
            IMessageSender<UserData, SendMessage> sender
    ) {
        super(sender);
    }

    @Override
    public TerminalMenu getOption(String optionStr) {
        return TerminalMenu.valueOf(optionStr);
    }

    @Override
    public TerminalMenu[] getOptions() {
        return TerminalMenu.values();
    }

    @Override
    public ExampleState executeCallback(TelegramClient bot, Update update, UserData userData, TerminalMenu t) {
        return returnState();
    }

    public abstract ExampleState returnState();
}
