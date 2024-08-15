package com.oierbravo.watercondenser.item;


import com.oierbravo.watercondenser.WaterCondenser;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WaterCondenser.MODID);


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
