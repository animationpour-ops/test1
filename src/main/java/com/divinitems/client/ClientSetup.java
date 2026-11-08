package com.divinitems.client;

import com.divinitems.DivineItems;
import com.divinitems.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Prédicats de modèle de l'arc (animation de bandage) — comme l'arc vanilla. */
@Mod.EventBusSubscriber(modid = DivineItems.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
	private ClientSetup() {}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(ModItems.DIVINE_BOW.get(), ResourceLocation.withDefaultNamespace("pull"),
				(stack, level, entity, seed) -> {
					if (entity == null || !entity.isUsingItem() || entity.getUseItem() != stack) return 0.0f;
					return entity.getTicksUsingItem() / 20.0f;
				});
			ItemProperties.register(ModItems.DIVINE_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"),
				(stack, level, entity, seed) ->
					entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0f : 0.0f);
		});
	}
}
