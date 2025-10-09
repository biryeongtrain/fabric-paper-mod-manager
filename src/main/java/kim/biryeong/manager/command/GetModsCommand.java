package kim.biryeong.manager.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.commands.CommandSourceStack;

public class GetModsCommand {
    public static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var source = ctx.getSource();
        var miniMessage = MiniMessage.miniMessage();
        List<Component> components = new ArrayList<>();

        components.add(miniMessage.deserialize("<white>===== Current Mods ===== </white>"));
        FabricLoader.getInstance().getAllMods().forEach(mod -> {
            ModMetadata meta = mod.getMetadata();
            components.add(miniMessage.deserialize("<hover:show_text:'<reset>Author : %s \n\nDescription : %s'><green> - ".formatted(Arrays.toString(meta.getAuthors().stream().map(Person::getName).toArray()), meta.getDescription()) + meta.getName() + "</green>"));
        });
        components.add(miniMessage.deserialize("<white>======================== </white>"));

        components.stream().map(PaperAdventure::asVanilla).forEach(source::sendSystemMessage);
        return 1;
    }

}
