package kim.biryeong.manager.impl.registry.utils;

import net.minecraft.resources.ResourceLocation;

public class RegSyncImplUtils {
    public static boolean isVanillaId(ResourceLocation id) {
        return id.getNamespace().equals("minecraft") || id.getNamespace().equals("brigadier");
    }
}
