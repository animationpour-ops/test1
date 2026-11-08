package com.divinitems;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DivineItems.MOD_ID)
public class DivineItems {
	public static final String MOD_ID = "divinitems";
	public static final Logger LOGGER = LogUtils.getLogger();

	public DivineItems() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.BLOCKS.register(modBus);
		ModArmorMaterials.MATERIALS.register(modBus);
		ModItems.ITEMS.register(modBus);
		ModCreativeTabs.TABS.register(modBus);
		LOGGER.info("Items Divins chargé.");
	}
}
