package com.divinitems;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Forge céleste. Clic droit avec un équipement en diamant (ou un arc) en main
 * + une Étoile divine dans l'inventaire = le même équipement en version divine.
 */
public class CelestialForgeBlock extends Block {
	public static final MapCodec<CelestialForgeBlock> CODEC = simpleCodec(CelestialForgeBlock::new);
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 13, 16);

	public CelestialForgeBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
											  Player player, InteractionHand hand, BlockHitResult hit) {
		if (!ForgeUpgrades.canUpgrade(stack)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!level.isClientSide) {
			ForgeUpgrades.tryForge(level, pos, player, stack);
		}
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide) {
			player.displayClientMessage(Component.translatable("message.divinitems.forge_hint").withStyle(ChatFormatting.LIGHT_PURPLE), true);
		}
		return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
	}

	/** Particules ambiantes (côté client). */
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextInt(3) == 0) {
			level.addParticle(ParticleTypes.END_ROD,
				pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.9,
				pos.getY() + 0.7 + random.nextDouble() * 0.4,
				pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.9,
				0.0, 0.03, 0.0);
		}
		if (random.nextInt(8) == 0) {
			level.addParticle(ParticleTypes.ENCHANT,
				pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
				(random.nextDouble() - 0.5) * 2.0, random.nextDouble(), (random.nextDouble() - 0.5) * 2.0);
		}
	}
}
