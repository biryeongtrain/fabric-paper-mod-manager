package kim.biryeong.manager.event.api.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionEvent<T,R> {
    private final ArrayList<T> handlers = new ArrayList<>();

    public T register(T listener) {
        this.handlers.add(listener);
        return listener;
    }

    public void unregister(T listener) {
        this.handlers.remove(listener);
    }

    public R invoke(Function<Collection<T>, R> invoker) {
        return invoker.apply(handlers);
    }

    public boolean isEmpty() {
        return this.handlers.isEmpty();
    }

    public Collection<T> invokers() {
        return Collections.unmodifiableCollection(this.handlers);
    }
}
