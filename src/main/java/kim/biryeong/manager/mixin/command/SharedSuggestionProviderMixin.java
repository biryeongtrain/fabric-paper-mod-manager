package kim.biryeong.manager.mixin.command;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.commands.SharedSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SharedSuggestionProvider.class)
public interface SharedSuggestionProviderMixin {
    @ModifyExpressionValue(method = "filterResources(Ljava/lang/Iterable;Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Consumer;)V",
    at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 0))
    private static boolean manager$cancelNamespaceCheck(boolean original) {
        return true;
    }
}
