package com.divinitems;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;

/** Armure divine : mêmes valeurs que la Netherite (3 armure / 3 robustesse / 10% recul au casque, comme dans la vidéo). */
public final class ModArmorMaterials {
	private ModArmorMaterials() {}

	public static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, DivineItems.MOD_ID);

	public static final RegistryObject<ArmorMaterial> DIVINE = MATERIALS.register("divine", () -> {
		EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
		defense.put(ArmorItem.Type.BOOTS, 3);
		defense.put(ArmorItem.Type.LEGGINGS, 6);
		defense.put(ArmorItem.Type.CHESTPLATE, 8);
		defense.put(ArmorItem.Type.HELMET, 3);
		defense.put(ArmorItem.Type.BODY, 11);
		return new ArmorMaterial(
			defense,
			30,
			SoundEvents.ARMOR_EQUIP_NETHERITE,
			() -> Ingredient.of(ModItems.DIVINE_STAR.get()),
			// textures/models/armor/divine_layer_1.png et divine_layer_2.png
			List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(DivineItems.MOD_ID, "divine"))),
			3.0f,
			0.1f
		);
	});
}
