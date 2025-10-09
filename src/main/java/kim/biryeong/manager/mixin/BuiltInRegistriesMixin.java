package kim.biryeong.manager.mixin;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "createContents", at = @At("RETURN"))
    private static void onBootstrap(CallbackInfo ci) {
        FabricLoader.getInstance().invokeEntrypoints("main", ModInitializer.class, ModInitializer::onInitialize);
    }
}
