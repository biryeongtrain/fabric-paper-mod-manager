package kim.biryeong.manager.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.adventure.PaperAdventure;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class GetModsCommand {
    public static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var source = ctx.getSource();
        var miniMessage = MiniMessage.miniMessage();
        List<Component> components = new ArrayList<>();

        components.add(miniMessage.deserialize("<white>===== Current Mods ===== </white>"));
        FabricLoader.getInstance().getAllMods().forEach(mod -> components.add(miniMessage.deserialize("<green> - " + mod.getMetadata().getName() + "</green>")));
        components.add(miniMessage.deserialize("<white>======================== </white>"));

        components.stream().map(PaperAdventure::asVanilla).forEach(source::sendSystemMessage);
        return 1;
    }

}
