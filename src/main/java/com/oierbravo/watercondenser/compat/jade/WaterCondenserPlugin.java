package com.oierbravo.watercondenser.compat.jade;

import com.oierbravo.melter.content.melter.MelterBlock;
import com.oierbravo.melter.content.melter.MelterBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MelterPlugin implements IWailaPlugin {
    public static final ResourceLocation MELTER_DATA = ResourceLocation.fromNamespaceAndPath("melter","melter_data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ProgressComponentProvider(), MelterBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ProgressComponentProvider(), MelterBlock.class);
    }
}
