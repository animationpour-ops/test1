package com.divinitems.items;

import com.divinitems.DivineText;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Hache divine : Abattage céleste (arbre entier, géré dans VeinMiner). */
public class DivineAxeItem extends AxeItem {
	public DivineAxeItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(DivineText.levelLine());
		DivineText.abilityLines(tooltip, "celestial_felling");
	}
}
