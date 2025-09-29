package kim.biryeong.manager.mixin;

import com.mojang.brigadier.tree.LiteralCommandNode;
import joptsimple.OptionSet;
import kim.biryeong.manager.api.command.CommandRegistrationCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/Bootstrap;validate()V", shift = At.Shift.AFTER))
    private static void manager$executeInitializer(OptionSet optionSet, CallbackInfo ci) {
        FabricLoader.getInstance().invokeEntrypoints("main", ModInitializer.class, ModInitializer::onInitialize);
    }
}
