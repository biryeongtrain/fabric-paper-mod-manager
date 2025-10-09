package kim.biryeong.manager.mixin.gametest;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixer;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import kim.biryeong.manager.gametest.impl.FabricGameTestRunner;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureTemplateManager.class)
public abstract class StructureTemplateManagerMixin {

    @Shadow
    private ResourceManager resourceManager;

    @Shadow
    public abstract StructureTemplate readStructure(CompoundTag nbt);

    @Unique
    Optional<StructureTemplate> fabric_loadSnbtFromResource(ResourceLocation resourceLocation) {
        ResourceLocation path = FabricGameTestRunner.GAMETEST_STRUCUTURE_FINDER.fileToId(resourceLocation);
        Optional<Resource> resource = this.resourceManager.getResource(path);

        if (resource.isPresent()) {
            try {
                String snbt = IOUtils.toString(resource.get().openAsReader());
                CompoundTag nbt = NbtUtils.snbtToStructure(snbt);
                return Optional.of(this.readStructure(nbt));
            } catch (IOException | CommandSyntaxException e) {
                throw new RuntimeException("Failed to load structure from " + path, e);
            }
        }

        return Optional.empty();
    }

    @Unique
    private Stream<ResourceLocation> streamTemplatesFromResource() {
        FileToIdConverter finder = FabricGameTestRunner.GAMETEST_STRUCUTURE_FINDER;
        return finder.listMatchingResources(this.resourceManager).keySet().stream().map(finder::fileToId);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;", ordinal = 2, shift = At.Shift.AFTER, remap = false))
    private void addFabricTemplateProvider(ResourceManager resourceManager, LevelStorageSource.LevelStorageAccess levelStorageAccess, DataFixer fixerUpper, HolderGetter blockLookup, CallbackInfo ci, @Local ImmutableList.Builder<StructureTemplateManager.Source> builder) {
        builder.add(new StructureTemplateManager.Source(this::fabric_loadSnbtFromResource, this::streamTemplatesFromResource));
    }
}
