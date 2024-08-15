package com.oierbravo.watercondenser.network.packets.data;

import com.oierbravo.watercondenser.WaterCondenser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidSyncPayload(
        FluidStack fluidStack,
        BlockPos pos
    ) implements CustomPacketPayload {

    public static final Type<FluidSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(WaterCondenser.MODID, "fluidsync_payload"));

    @Override
    public Type<FluidSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidSyncPayload> STREAM_CODEC = StreamCodec.composite(
            FluidStack.STREAM_CODEC, FluidSyncPayload::fluidStack,
            BlockPos.STREAM_CODEC, FluidSyncPayload::pos,
            FluidSyncPayload::new
    );
}
