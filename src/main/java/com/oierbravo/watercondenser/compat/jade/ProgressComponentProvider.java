package com.oierbravo.watercondenser.compat.jade;

import com.oierbravo.melter.content.melter.MelterBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ProgressStyle;

import java.awt.*;

public class ProgressComponentProvider  implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //CompoundTag serverData = accessor.getServerData();
        if (accessor.getServerData().contains("melter.progress")) {
            int progress = accessor.getServerData().getInt("melter.progress");
            IElementHelper elementHelper = IElementHelper.get();
            ProgressStyle progressStyle = elementHelper.progressStyle().textColor(1);
            if(progress > 0)
                tooltip.add(elementHelper.progress((float)progress / 100, Component.translatable("melter.tooltip.progress", progress).withColor(Color.GRAY.getRGB()), progressStyle.color(Color.YELLOW.getRGB()), BoxStyle.getTransparent(), false));
            int heatLevel = accessor.getServerData().getInt("melter.heat_level");
            boolean isCreative = accessor.getServerData().getBoolean("melter.creative");
            if (isCreative) {
                tooltip.add(Component.translatable("melter.tooltip.heat_level").append(" ").append(Component.translatable("melter.tooltip.heat_level.creative")));
            }
            else if(heatLevel > 0) {
                tooltip.add(Component.translatable("melter.tooltip.heat_level").append(Component.literal(" " + heatLevel).withStyle(ChatFormatting.GOLD)));
            } else {
                tooltip.add(Component.translatable("melter.tooltip.heat_level.none"));
            }
        }

    }

    @Override
    public ResourceLocation getUid() {
        return MelterPlugin.MELTER_DATA;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();

        if (blockEntity != null) {
            MelterBlockEntity melter = (MelterBlockEntity) blockEntity;

            BlockState blockStateBelow = melter.getLevel().getBlockState(melter.getBlockPos().below());
            Block below = blockStateBelow.getBlock();
            String blockName = below.getName().getString();

            compoundTag.putInt("melter.progress", melter.getProgressPercent());
            compoundTag.putString("melter.heat_source_name", blockName);
            compoundTag.putInt("melter.heat_level", melter.getHeatLevel());
            compoundTag.putBoolean("melter.creative", melter.isCreative());
        }
    }
}
