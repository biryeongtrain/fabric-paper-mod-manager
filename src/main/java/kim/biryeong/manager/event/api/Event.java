package kim.biryeong.manager.event.api;

import net.minecraft.resources.ResourceLocation;

/**
 * imported from Fabric Event API
 * @param <T>
 */
public abstract class Event<T> {
    protected volatile T invoker;
    public T invoker() {
        return invoker;
    }
    public abstract void register(T listener);
    public static final ResourceLocation EVENT_BUS = ResourceLocation.fromNamespaceAndPath("fabric", "default");

    public void register(ResourceLocation phase, T listener) {
        register(listener);
    }
    public void addPhaseOrdering(ResourceLocation firstPhase, ResourceLocation secondPhase) {
    }
}
