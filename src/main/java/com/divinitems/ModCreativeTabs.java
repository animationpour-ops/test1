package com.divinitems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
	private ModCreativeTabs() {}

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DivineItems.MOD_ID);

	public static final RegistryObject<CreativeModeTab> DIVINE = TABS.register("divine",
		() -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.divinitems"))
			.icon(() -> new ItemStack(ModItems.DIVINE_SWORD.get()))
			.displayItems((params, output) -> {
				output.accept(ModItems.CELESTIAL_FORGE.get());
				output.accept(ModItems.DIVINE_STAR.get());
				output.accept(ModItems.DIVINE_SWORD.get());
				output.accept(ModItems.DIVINE_PICKAXE.get());
				output.accept(ModItems.DIVINE_AXE.get());
				output.accept(ModItems.DIVINE_BOW.get());
				output.accept(ModItems.DIVINE_HELMET.get());
				output.accept(ModItems.DIVINE_CHESTPLATE.get());
				output.accept(ModItems.DIVINE_LEGGINGS.get());
				output.accept(ModItems.DIVINE_BOOTS.get());
			})
			.build());
}
