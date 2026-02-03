package cs.youtrade.telegram.prototype;

import cs.youtrade.telegram.buttons.def.DefStateInt;
import cs.youtrade.telegram.buttons.menu.BaseMenuRegistry;
import cs.youtrade.telegram.menu.ExampleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TelegramConfig {
    @Bean
    public BaseMenuRegistry<UserData, ExampleState> userMenuRegistry(
            @Autowired List<DefStateInt<UserData, ExampleState, ?>> commands
    ) {
        return new BaseMenuRegistry<>(commands) {
        };
    }
}
