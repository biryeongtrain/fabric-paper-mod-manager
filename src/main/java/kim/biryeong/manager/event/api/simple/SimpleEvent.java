package kim.biryeong.manager.event.api.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class SimpleEvent<T> {
    private final ArrayList<T> handlers = new ArrayList<>();

    public T register(T listener) {
        this.handlers.add(listener);
        return listener;
    }

    public void unregister(T listener) {
        this.handlers.remove(listener);
    }

    public void invoke(Consumer<T> invoker) {
        for (var handler : handlers) {
            invoker.accept(handler);
        }
    }

    public boolean isEmpty() {
        return this.handlers.isEmpty();
    }

    public Collection<T> invokers() {
        return Collections.unmodifiableCollection(this.handlers);
    }
}
