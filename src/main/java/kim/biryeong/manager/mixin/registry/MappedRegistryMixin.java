package kim.biryeong.manager.mixin.registry;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import java.util.ArrayList;
import java.util.Set;
import kim.biryeong.manager.api.reg.RegistrySyncUtils;
import kim.biryeong.manager.impl.registry.RegistrySyncExtension;
import kim.biryeong.manager.impl.registry.utils.RegSyncImplUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements RegistrySyncExtension<T>, WritableRegistry<T> {
    @Unique
    private final Reference2BooleanOpenHashMap<T> manager$entryStatus = new Reference2BooleanOpenHashMap<>();
    @Shadow
    public abstract @NotNull Set<ResourceLocation> keySet();

    @Shadow
    @Final
    private ObjectList<Holder.Reference<T>> byId;
    @Shadow
    @Final
    private Reference2IntMap<T> toId;

    @Shadow
    protected abstract void validateWrite();

    @Unique
    private RegistrySyncExtension.Status registryStatus = null;
    @Unique
    private boolean alreadyOrdered = false;

    @Inject(method = "register", at = @At("TAIL"))
    private <V extends T> void resetStatusOnAdd(ResourceKey<T> key, T value, RegistrationInfo registrationInfo, CallbackInfoReturnable<Holder.Reference<T>> cir) {
        this.registryStatus = null;
        this.alreadyOrdered = false;
    }

    @Inject(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/util/Map;isEmpty()Z"))
    private void reorderOnFreeze(CallbackInfoReturnable<Set<ResourceKey<T>>> cir) {
        this.manager$reorderEntries();
    }

    @Override
    public void manager$reorderEntries() {
        if (this.manager$entryStatus.isEmpty() || alreadyOrdered) {
            return;
        }

        var vanilla = new ArrayList<Holder.Reference<T>>();
        var modded = new ArrayList<Holder.Reference<T>>();

        for (var entry : this.byId) {
            if (this.manager$isServerEntry(entry.value())) {
                modded.add(entry);
            } else {
                vanilla.add(entry);
            }
        }

        this.byId.clear();
        this.byId.addAll(vanilla);
        this.byId.addAll(modded);
        this.toId.clear();

        for (var i  = 0; i < this.byId.size(); i++) {
            this.toId.put(this.byId.get(i).value(), i);
        }
        this.alreadyOrdered = true;
    }

    @Override
    public void manager$clearStatus() {
        this.registryStatus = null;
    }

    @Override
    public Status manager$getStatus() {
        if (this.registryStatus == null) {
            var status = Status.VANILLA;
            for (var id : this.keySet()) {
                if (RegSyncImplUtils.isVanillaId(id)) {
                    continue;
                }

                if (RegistrySyncUtils.isServerEntry(this, id)) {
                    status = Status.WITH_SERVER_ONLY;
                } else {
                    status = Status.WITH_MODDED;
                    break;
                }

                this.registryStatus = status;
            }
        }
        return this.registryStatus;
    }

    @Override
    public boolean manager$isServerEntry(T obj) {
        return this.manager$entryStatus.getBoolean(obj);
    }



    @Override
    public void manager$setServerEntry(T obj, boolean value) {
        this.validateWrite();
        this.registryStatus = null;
        this.manager$entryStatus.put(obj, value);
    }

    @Override
    public void manager$setStatus(Status status) {
        this.registryStatus = status;
    }

    @Override
    public boolean manager$updateStatus(Status status) {
        return false;
    }
}
