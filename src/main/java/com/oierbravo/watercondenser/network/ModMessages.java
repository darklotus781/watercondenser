package com.oierbravo.watercondenser.network;

import com.oierbravo.watercondenser.WaterCondenser;
import com.oierbravo.watercondenser.network.packets.data.FluidSyncPayload;
import com.oierbravo.watercondenser.network.packets.handler.FluidSyncPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModMessages {
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WaterCondenser.MODID);

        //Going to Client
        registrar.playToClient(FluidSyncPayload.TYPE, FluidSyncPayload.STREAM_CODEC, FluidSyncPacket.get()::handle);
    }
    public static void sendToAllClients(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }

    public static void register() {
    }
}
