package com.divinitems;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

import java.util.List;

/** Textes communs : tooltip "Niveau Divin" arc-en-ciel, lignes de capacités, messages de barre d'action. */
public final class DivineText {
	private DivineText() {}

	/** Texte arc-en-ciel, une couleur par lettre. */
	public static MutableComponent rainbow(String s) {
		MutableComponent out = Component.empty();
		int n = Math.max(1, s.length());
		for (int i = 0; i < s.length(); i++) {
			int rgb = Mth.hsvToRgb(i / (float) n, 0.85f, 1.0f);
			out.append(Component.literal(String.valueOf(s.charAt(i))).setStyle(Style.EMPTY.withColor(rgb)));
		}
		return out;
	}

	/** "✦ Niveau Divin ✦" */
	public static MutableComponent levelLine() {
		return Component.literal("✦ ").withStyle(ChatFormatting.LIGHT_PURPLE)
			.append(rainbow(Component.translatable("tooltip.divinitems.level").getString()))
			.append(Component.literal(" ✦").withStyle(ChatFormatting.LIGHT_PURPLE));
	}

	/** Ajoute le nom + la description de chaque capacité au tooltip. */
	public static void abilityLines(List<Component> tooltip, String... ids) {
		for (String id : ids) {
			tooltip.add(Component.literal("✦ ").withStyle(ChatFormatting.GOLD)
				.append(Component.translatable("ability.divinitems." + id).withStyle(ChatFormatting.GOLD)));
			tooltip.add(Component.literal("  ").append(Component.translatable("ability.divinitems." + id + ".desc")).withStyle(ChatFormatting.GRAY));
		}
	}

	/** "✦ Nom de la capacité ✦" pour la barre d'action. */
	public static Component actionBar(String id) {
		return Component.literal("✦ ")
			.append(Component.translatable("ability.divinitems." + id))
			.append(Component.literal(" ✦"))
			.withStyle(ChatFormatting.AQUA);
	}
}
