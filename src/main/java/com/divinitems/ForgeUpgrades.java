package com.divinitems;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

/** Table de forge : équipement en diamant -> équipement divin (coût : 1 Étoile divine). */
public final class ForgeUpgrades {
	private ForgeUpgrades() {}

	private static Map<Item, Item> map;

	private static Map<Item, Item> map() {
		if (map == null) {
			map = new HashMap<>();
			map.put(Items.DIAMOND_SWORD, ModItems.DIVINE_SWORD.get());
			map.put(Items.DIAMOND_PICKAXE, ModItems.DIVINE_PICKAXE.get());
			map.put(Items.DIAMOND_AXE, ModItems.DIVINE_AXE.get());
			map.put(Items.BOW, ModItems.DIVINE_BOW.get());
			map.put(Items.DIAMOND_HELMET, ModItems.DIVINE_HELMET.get());
			map.put(Items.DIAMOND_CHESTPLATE, ModItems.DIVINE_CHESTPLATE.get());
			map.put(Items.DIAMOND_LEGGINGS, ModItems.DIVINE_LEGGINGS.get());
			map.put(Items.DIAMOND_BOOTS, ModItems.DIVINE_BOOTS.get());
		}
		return map;
	}

	public static boolean canUpgrade(ItemStack stack) {
		return map().containsKey(stack.getItem());
	}

	public static void tryForge(Level level, BlockPos pos, Player player, ItemStack held) {
		if (!(level instanceof ServerLevel sw)) return;
		Item resultItem = map().get(held.getItem());
		if (resultItem == null) return;

		int starSlot = -1;
		Inventory inv = player.getInventory();
		if (!player.isCreative()) {
			for (int i = 0; i < inv.getContainerSize(); i++) {
				if (inv.getItem(i).is(ModItems.DIVINE_STAR.get())) { starSlot = i; break; }
			}
			if (starSlot < 0) {
				player.displayClientMessage(Component.translatable("message.divinitems.need_star").withStyle(ChatFormatting.RED), true);
				return;
			}
		}

		// Consommation
		if (starSlot >= 0) inv.getItem(starSlot).shrink(1);
		held.shrink(1);

		// Résultat + enchantements
		ItemStack result = new ItemStack(resultItem);
		applyEnchants(sw, result);
		if (!player.getInventory().add(result)) {
			player.drop(result, false);
		}

		// Effets visuels / sonores
		Vec3 top = Vec3.atCenterOf(pos).add(0, 0.5, 0);
		Abilities.pillar(sw, top, 12.0);
		Abilities.ring(sw, ParticleTypes.END_ROD, top, 1.2, 24);
		Abilities.ring(sw, Abilities.GOLD_DUST, top.add(0, 0.5, 0), 0.9, 18);
		sw.sendParticles(ParticleTypes.FLASH, top.x, top.y + 0.5, top.z, 1, 0, 0, 0, 0);
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sw);
		if (bolt != null) {
			bolt.moveTo(Vec3.atBottomCenterOf(pos.above()));
			bolt.setVisualOnly(true);
			sw.addFreshEntity(bolt);
		}
		sw.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.5f, 1.2f);
		sw.playSound(null, pos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 1.0f, 1.0f);
		player.displayClientMessage(Component.translatable("message.divinitems.forged").withStyle(ChatFormatting.GOLD), true);
	}

	private static void applyEnchants(ServerLevel w, ItemStack s) {
		Item i = s.getItem();
		if (i == ModItems.DIVINE_SWORD.get()) {
			add(w, s, Enchantments.SHARPNESS, 5);
			add(w, s, Enchantments.LOOTING, 3);
			add(w, s, Enchantments.UNBREAKING, 3);
		} else if (i == ModItems.DIVINE_PICKAXE.get()) {
			add(w, s, Enchantments.EFFICIENCY, 5);
			add(w, s, Enchantments.FORTUNE, 3);
			add(w, s, Enchantments.UNBREAKING, 3);
		} else if (i == ModItems.DIVINE_AXE.get()) {
			add(w, s, Enchantments.EFFICIENCY, 5);
			add(w, s, Enchantments.UNBREAKING, 3);
		} else if (i == ModItems.DIVINE_BOW.get()) {
			add(w, s, Enchantments.POWER, 5);
			add(w, s, Enchantments.UNBREAKING, 3);
		} else {
			// Armure : Protection X comme sur le casque de la vidéo.
			add(w, s, Enchantments.PROTECTION, 10);
			add(w, s, Enchantments.UNBREAKING, 3);
		}
	}

	private static void add(ServerLevel w, ItemStack stack, ResourceKey<Enchantment> key, int level) {
		w.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(key).ifPresent(e -> stack.enchant(e, level));
	}
}
