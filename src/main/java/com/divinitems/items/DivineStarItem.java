package com.divinitems.items;

import com.divinitems.DivineText;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Étoile divine : composant de base pour forger les items divins. */
public class DivineStarItem extends Item {
	public DivineStarItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(DivineText.levelLine());
		tooltip.add(Component.translatable("tooltip.divinitems.star").withStyle(ChatFormatting.GRAY));
	}
}
