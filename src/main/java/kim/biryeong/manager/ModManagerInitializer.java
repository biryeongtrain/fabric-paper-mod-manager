package kim.biryeong.manager;

import com.mojang.brigadier.tree.LiteralCommandNode;
import kim.biryeong.manager.api.command.CommandRegistrationCallback;
import kim.biryeong.manager.command.GetModsCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModManagerInitializer implements ModInitializer {
    public static final String MOD_ID = "mod-manager";
    public static final Logger LOGGER = LoggerFactory.getLogger("Mod Manager");
    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric / Paper World!");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralCommandNode<CommandSourceStack> root = Commands.literal("manager").build();

            LiteralCommandNode<CommandSourceStack> mods = Commands.literal("mods")
                    .requires(source -> source.hasPermission(2))
                    .executes(GetModsCommand::execute)
                    .build();

            root.addChild(mods);
            dispatcher.getRoot().addChild(root);
        });
    }
}
