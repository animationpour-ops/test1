package com.divinitems;

import com.divinitems.items.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
	private ModItems() {}

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DivineItems.MOD_ID);

	private static Item.Properties divine() {
		return new Item.Properties().fireResistant().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
	}

	public static final RegistryObject<Item> DIVINE_STAR = ITEMS.register("divine_star",
		() -> new DivineStarItem(divine()));

	public static final RegistryObject<Item> DIVINE_SWORD = ITEMS.register("divine_sword",
		() -> new DivineSwordItem(DivineTier.INSTANCE,
			divine().attributes(SwordItem.createAttributes(DivineTier.INSTANCE, 3, -2.4f))));

	public static final RegistryObject<Item> DIVINE_PICKAXE = ITEMS.register("divine_pickaxe",
		() -> new DivinePickaxeItem(DivineTier.INSTANCE,
			divine().attributes(PickaxeItem.createAttributes(DivineTier.INSTANCE, 1.0f, -2.8f))));

	public static final RegistryObject<Item> DIVINE_AXE = ITEMS.register("divine_axe",
		() -> new DivineAxeItem(DivineTier.INSTANCE,
			divine().attributes(AxeItem.createAttributes(DivineTier.INSTANCE, 5.0f, -3.0f))));

	public static final RegistryObject<Item> DIVINE_BOW = ITEMS.register("divine_bow",
		() -> new DivineBowItem(divine().durability(2500)));

	public static final RegistryObject<Item> DIVINE_HELMET = ITEMS.register("divine_helmet",
		() -> new DivineArmorItem(ModArmorMaterials.DIVINE.getHolder().orElseThrow(), ArmorItem.Type.HELMET,
			divine().durability(ArmorItem.Type.HELMET.getDurability(50))));
	public static final RegistryObject<Item> DIVINE_CHESTPLATE = ITEMS.register("divine_chestplate",
		() -> new DivineArmorItem(ModArmorMaterials.DIVINE.getHolder().orElseThrow(), ArmorItem.Type.CHESTPLATE,
			divine().durability(ArmorItem.Type.CHESTPLATE.getDurability(50))));
	public static final RegistryObject<Item> DIVINE_LEGGINGS = ITEMS.register("divine_leggings",
		() -> new DivineArmorItem(ModArmorMaterials.DIVINE.getHolder().orElseThrow(), ArmorItem.Type.LEGGINGS,
			divine().durability(ArmorItem.Type.LEGGINGS.getDurability(50))));
	public static final RegistryObject<Item> DIVINE_BOOTS = ITEMS.register("divine_boots",
		() -> new DivineArmorItem(ModArmorMaterials.DIVINE.getHolder().orElseThrow(), ArmorItem.Type.BOOTS,
			divine().durability(ArmorItem.Type.BOOTS.getDurability(50))));

	public static final RegistryObject<Item> CELESTIAL_FORGE = ITEMS.register("celestial_forge",
		() -> new DivineBlockItem(ModBlocks.CELESTIAL_FORGE.get(), divine()));
}
