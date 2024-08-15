package com.oierbravo.watercondenser.entity;

import com.oierbravo.watercondenser.WaterCondenser;
import com.oierbravo.watercondenser.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WaterCondenser.MODID);


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WatercondenserBlockEntity>> WATERCONDENSER_ENTITY =
            BLOCK_ENTITIES.register("watercondenser_entity", () ->
                    BlockEntityType.Builder.of(WatercondenserBlockEntity::new,
                            ModBlocks.WATERCONDENSER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
