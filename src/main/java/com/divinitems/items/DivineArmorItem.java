package com.divinitems.items;

import com.divinitems.DivineText;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Pièce d'armure divine. Les effets sont appliqués dans ArmorEffects. */
public class DivineArmorItem extends ArmorItem {
	public DivineArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
		super(material, type, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(DivineText.levelLine());
		switch (this.getType()) {
			case HELMET -> DivineText.abilityLines(tooltip, "night_vision", "authoritative_gaze");
			case CHESTPLATE -> DivineText.abilityLines(tooltip, "celestial_presence");
			case LEGGINGS -> DivineText.abilityLines(tooltip, "celestial_stride");
			case BOOTS -> DivineText.abilityLines(tooltip, "divine_step");
			default -> { }
		}
	}
}
