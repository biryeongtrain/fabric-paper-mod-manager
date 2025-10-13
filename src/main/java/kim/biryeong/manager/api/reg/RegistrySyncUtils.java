package kim.biryeong.manager.api.reg;

import kim.biryeong.manager.impl.registry.RegistrySyncExtension;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class RegistrySyncUtils {
    private RegistrySyncUtils() { }

    public static <T> boolean isServerEntry(Registry<T> registry, T entry) {
        if (registry instanceof RegistrySyncExtension<?>) {
            return ((RegistrySyncExtension<T>) registry).manager$isServerEntry(entry);
        } else {
            return false;
        }
    }

    public static <T> boolean isServerEntry(Registry<T> registry, ResourceLocation id) {
        return registry.containsKey(id) ? isServerEntry(registry, registry.getValue(id)) : false;
    }

    public static <T> void setServerEntry(Registry<T> registry, T entry) {
        if (registry instanceof RegistrySyncExtension<?>) {
            ((RegistrySyncExtension<T>) registry).manager$setServerEntry(entry, true);
        }
    }

    public static <T> void setServerEntry(Registry<T> registry, ResourceLocation id) {
        if (registry.containsKey(id)) {
            setServerEntry(registry, registry.getValue(id));
        } else {
            throw new IllegalArgumentException("Entry '" + id + "' of registry '" + registry.key().location() + "' isn't registered!");
        }
    }
}
