package com.oierbravo.watercondenser;


import com.oierbravo.watercondenser.block.custom.WatercondenserRenderer;
import com.oierbravo.watercondenser.entity.ModBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = WaterCondenser.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        WaterCondenser.LOGGER.info("INIT CLIENT SETUP");
        WatercondenserRenderer.register();
    }
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.WATERCONDENSER_ENTITY.get(), WatercondenserRenderer::new);
    }
}