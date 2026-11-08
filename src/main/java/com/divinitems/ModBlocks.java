package com.divinitems;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
	private ModBlocks() {}

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DivineItems.MOD_ID);

	public static final RegistryObject<Block> CELESTIAL_FORGE = BLOCKS.register("celestial_forge",
		() -> new CelestialForgeBlock(BlockBehaviour.Properties.of()
			.strength(3.5f, 12.0f)
			.noOcclusion()
			.lightLevel(state -> 12)
			.sound(SoundType.AMETHYST)));
}
