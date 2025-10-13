package kim.biryeong.manager.mixin.registry;

import kim.biryeong.manager.impl.registry.RegistrySyncExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;startTimerHackThread()V"))
    private static void afterModInit(CallbackInfo ci) {
        for (var reg : BuiltInRegistries.REGISTRY) {
            if (reg instanceof RegistrySyncExtension<?> ext) {
                ext.manager$reorderEntries();
            }
        }
    }
}
