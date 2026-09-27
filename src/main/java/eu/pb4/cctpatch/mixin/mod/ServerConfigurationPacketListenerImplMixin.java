package eu.pb4.cctpatch.mixin.mod;

import eu.pb4.cctpatch.impl.poly.res.OptionalResourcePackConfigurationTask;
import eu.pb4.cctpatch.impl.poly.res.ResourcePackGenerator;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class ServerConfigurationPacketListenerImplMixin {
    @Shadow
    private @Nullable ConfigurationTask currentTask;

    @Shadow
    protected abstract void finishCurrentTask(ConfigurationTask.Type taskTypeToFinish);

    @Inject(method = "handleResourcePackResponse", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerCommonPacketListenerImpl;handleResourcePackResponse(Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onStatus(ServerboundResourcePackPacket packet, CallbackInfo ci) {
        if (this.currentTask instanceof OptionalResourcePackConfigurationTask) {
            if (packet.id().equals(ResourcePackGenerator.UUID) && packet.action().isTerminal()) {
                this.finishCurrentTask(OptionalResourcePackConfigurationTask.TYPE);
            }
            ci.cancel();
        }
    }
}
