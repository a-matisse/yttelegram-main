package cs.youtrade.telegram.buttons.menu;

import cs.youtrade.telegram.buttons.data.AbstractUserData;
import cs.youtrade.telegram.buttons.def.DefStateInt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseMenuRegistry<T extends AbstractUserData, STATE extends Enum<STATE>> {
    private final Map<STATE, DefStateInt<T, STATE, ?>> registry = new ConcurrentHashMap<>();

    public BaseMenuRegistry(List<DefStateInt<T, STATE, ?>> commands) {
        for (DefStateInt<T, STATE, ?> c : commands)
            registry.put(c.supportedState(), c);
    }

    public DefStateInt<T, STATE, ?> get(STATE state) {
        return registry.get(state);
    }
}
