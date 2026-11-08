package com.divinitems.items;

import com.divinitems.DivineText;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Pioche divine : Veine divine (minage de filon, géré dans VeinMiner). */
public class DivinePickaxeItem extends PickaxeItem {
	public DivinePickaxeItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(DivineText.levelLine());
		DivineText.abilityLines(tooltip, "divine_vein");
	}
}
