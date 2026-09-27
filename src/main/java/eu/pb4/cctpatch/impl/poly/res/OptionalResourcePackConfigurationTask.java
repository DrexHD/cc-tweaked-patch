package eu.pb4.cctpatch.impl.poly.res;

import eu.pb4.cctpatch.impl.util.OptionalClientNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

import java.util.Optional;
import java.util.function.Consumer;

public class OptionalResourcePackConfigurationTask implements ConfigurationTask {

    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("cc-tweaked:server_resource_pack");
    private final ServerConfigurationPacketListenerImpl listener;
    private final MinecraftServer.ServerResourcePackInfo info;
    private boolean shouldPush;

    public OptionalResourcePackConfigurationTask(ServerConfigurationPacketListenerImpl listener, MinecraftServer.ServerResourcePackInfo info) {
        this.listener = listener;
        this.info = info;
    }

    @Override
    public void start(Consumer<Packet<?>> connection) {
        this.shouldPush = !OptionalClientNetworking.canSyncRawToClient(this.listener);
        if (shouldPush) {
            connection.accept(new ClientboundResourcePackPushPacket(this.info.id(), this.info.url(), this.info.hash(), this.info.isRequired(), Optional.ofNullable(this.info.prompt())));
        }
    }

    @Override
    public boolean tick() {
        if (!shouldPush) {
            return true;
        }
        return ConfigurationTask.super.tick();
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
