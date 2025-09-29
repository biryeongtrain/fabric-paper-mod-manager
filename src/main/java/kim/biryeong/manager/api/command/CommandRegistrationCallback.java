package kim.biryeong.manager.api.command;

import com.mojang.brigadier.CommandDispatcher;
import kim.biryeong.manager.event.api.Event;
import kim.biryeong.manager.event.api.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface CommandRegistrationCallback {
    Event<CommandRegistrationCallback> EVENT = EventFactory.createArrayBacked(CommandRegistrationCallback.class, (callbacks) -> (dispatcher, registryAccess) -> {
        for (CommandRegistrationCallback callback : callbacks) {
            callback.register(dispatcher, registryAccess);
        }
    });

    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess);
}
