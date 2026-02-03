package cs.youtrade.telegram.buttons.state;

import cs.youtrade.telegram.buttons.data.AbstractUserData;

import java.util.function.Supplier;

public interface IStateRegistry<U extends AbstractUserData, B> {
    B getOrCreate(U userData, Supplier<B> dataSupplier);

    B get(U userData);

    B remove(U userData);

    boolean exists(U userData);
}
