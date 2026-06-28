package eu.pb4.cctpatch.mixin.mod.block;

import dan200.computercraft.shared.ModRegistry;
import dan200.computercraft.shared.peripheral.monitor.MonitorBlock;
import dan200.computercraft.shared.peripheral.monitor.MonitorBlockEntity;
import dan200.computercraft.shared.platform.RegistryEntry;
import dan200.computercraft.shared.util.BlockEntityHelpers;
import eu.pb4.cctpatch.impl.poly.ext.MonitorBlockEntityExt;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MonitorBlock.class)
public abstract class MonitorBlockMixin implements BlockEntityProvider {
    @Shadow
    @Final
    private RegistryEntry<? extends BlockEntityType<? extends MonitorBlockEntity>> type;
    @Unique
    private final BlockEntityTicker<MonitorBlockEntity> serverTicker = (level, pos, state, monitor) -> ((MonitorBlockEntityExt)monitor).updateWatchers();

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> type) {
        return level.isClient() ? null : BlockEntityHelpers.createTickerHelper(type, this.type.get(), serverTicker);
    }
}
