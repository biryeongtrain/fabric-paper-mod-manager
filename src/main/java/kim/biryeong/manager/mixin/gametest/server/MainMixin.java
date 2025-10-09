package kim.biryeong.manager.mixin.gametest.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import joptsimple.OptionSet;
import kim.biryeong.manager.gametest.impl.FabricGameTestRunner;
import net.minecraft.server.Main;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @ModifyExpressionValue(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/Eula;hasAgreedToEULA()Z"))
    private static boolean gametest$forceEulaToTrue(boolean original) {
        return FabricGameTestRunner.ENABLED || original;
    }

    @Inject(method = "main", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/packs/repository/ServerPacksSource;createPackRepository(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;)Lnet/minecraft/server/packs/repository/PackRepository;"), cancellable = true)
    private static void gametest$runGameTestServer(OptionSet optionSet, CallbackInfo ci, @Local LevelStorageSource.LevelStorageAccess levelStorageAccess, @Local PackRepository packRepository) {
        if (FabricGameTestRunner.ENABLED) {
            FabricGameTestRunner.runHeadlessServer(levelStorageAccess, packRepository);
            ci.cancel();
        }
    }

    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Throwable;)V", shift = At.Shift.AFTER))
    private static void exitOnError(CallbackInfo ci) {
        if (FabricGameTestRunner.ENABLED) {
            System.exit(-1);
        }
    }
}
