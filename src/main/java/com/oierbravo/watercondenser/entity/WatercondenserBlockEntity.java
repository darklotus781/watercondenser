package com.oierbravo.watercondenser.entity;

import com.oierbravo.watercondenser.config.ModConfigCommon;
import com.oierbravo.watercondenser.network.ModMessages;
import com.oierbravo.watercondenser.network.packets.data.FluidSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;


import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;
/**
 *  Code adapted from https://github.com/EwyBoy/ITank/blob/1.18.2/src/main/java/com/ewyboy/itank/common/content/tank/TankTile.java
 *
 */
public class WatercondenserBlockEntity extends BlockEntity {
    private static final RandomGenerator sharedRandom = new Random();
    private static Fluid fluidOutput = null;
    private static long lastCycleTime = -1;
    private static int cycleCounter = 0;
    private static boolean resetCycle = false;
    private CompoundTag updateTag;
    private final FluidTank fluidTankHandler = createFluidTank();

    private Lazy<IFluidHandler> lazyFluidHandler = Lazy.of(() -> fluidTankHandler);
    public WatercondenserBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.WATERCONDENSER_ENTITY.get(), pWorldPosition, pBlockState);
        updateTag = getPersistentData();
    }
    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        lazyFluidHandler.invalidate();
    }
    public static void verifyConfig(final Logger logger) {
        if (fluidOutput == null) {
            // verify and set the configured fluid
            final String fluidResourceRaw = ModConfigCommon.CONDENSER_FLUID.get();
            final ResourceLocation desiredFluid = ResourceLocation.parse(fluidResourceRaw);

            if (BuiltInRegistries.FLUID.containsKey(desiredFluid)) {
                fluidOutput = BuiltInRegistries.FLUID.get(desiredFluid);
            } else {
                logger.error("Unknown fluid '{}' in config, using default '{}' instead", fluidResourceRaw, ModConfigCommon.CONDENSER_FLUID_DEFAULT);
                fluidOutput = BuiltInRegistries.FLUID.get( ResourceLocation.parse(ModConfigCommon.CONDENSER_FLUID_DEFAULT));
            }
        }
    }
    private FluidTank createFluidTank() {
        return new FluidTank(ModConfigCommon.CONDENSER_CAPACITY.get(), ((FluidStack fluid) -> fluid.getFluid().isSame(fluidOutput))) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                assert level != null;
                if(!level.isClientSide()) {
                    ModMessages.sendToAllClients(new FluidSyncPayload(getFluidStack(), worldPosition));
                }
            }
        };
    }

    public FluidStack getFluidStack() {

        if (!fluidTankHandler.isEmpty()) {
            return fluidTankHandler.getFluid();
        }

        return new FluidStack(fluidOutput, 1);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        fluidTankHandler.writeToNBT(registries, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidTankHandler.readFromNBT(registries, tag);

        if (!fluidTankHandler.getFluid().getFluid().isSame(fluidOutput)) {
            // fluid in config differs from saved NBT, override it
            final FluidStack changedFluidStack = new FluidStack(fluidOutput, fluidTankHandler.getFluid().getAmount());
            fluidTankHandler.setFluid(changedFluidStack);
        }
    }

    public static <T extends BlockEntity> void tick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
        WatercondenserBlockEntity blockEntity = (WatercondenserBlockEntity) pBlockEntity;
        if(pLevel.isClientSide()) {
            return;
        }

        final long timeNow = pLevel.getDayTime();
        if (timeNow != lastCycleTime) {
            lastCycleTime = timeNow;
            if (resetCycle) {
                // A "lazy" counter reset; allows all TE's to actually get a chance to tick their cycle
                resetCycle = false;
                cycleCounter = 0;
            }

            cycleCounter++;
        }

        if (cycleCounter >= ModConfigCommon.CONDENSER_TICKS_PER_CYCLE.get()) {
            resetCycle = true;

            final float amountMultiMin = ModConfigCommon.CONDENSER_MB_MULTI_MIN.get();
            int amount = ModConfigCommon.CONDENSER_MB_PER_CYCLE.get();
            if (amountMultiMin < 1.0f) {
                final float randomMultiplier = amountMultiMin + (sharedRandom.nextFloat() * (ModConfigCommon.CONDENSER_MB_MULTI_MAX.get() - amountMultiMin));
                amount = Math.round(ModConfigCommon.CONDENSER_MB_PER_CYCLE.get() * randomMultiplier);
            }

            blockEntity.fluidTankHandler.fill( new FluidStack(fluidOutput, amount), IFluidHandler.FluidAction.EXECUTE);
        }
    }
    public IFluidHandler getFluidHandler() {
        return this.fluidTankHandler;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public void setFluid(FluidStack fluidStack) {

        this.fluidTankHandler.setFluid(fluidStack);
    }

    public boolean consumeWaterBottle() {
        int consumption = ModConfigCommon.CONDENSER_BOTTLE_MB_CONSUMPTION.get();
        if( consumption > fluidTankHandler.getFluidAmount()){
            return false;
        }
        fluidTankHandler.drain(consumption, IFluidHandler.FluidAction.EXECUTE);
        return true;
    }


}
