package com.divinitems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Veine divine (pioche) et Abattage céleste (hache). S'accroupir = minage normal.
 * Appelé AVANT la destruction du bloc d'origine (BlockEvent.BreakEvent) : on ne touche jamais au bloc d'origine.
 */
public final class VeinMiner {
	private VeinMiner() {}

	public static void onBlockBreak(ServerLevel level, Player player, BlockPos origin, BlockState state) {
		if (player.isShiftKeyDown()) return;
		ItemStack tool = player.getMainHandItem();

		if (tool.is(ModItems.DIVINE_PICKAXE.get()) && state.is(Tags.Blocks.ORES)) {
			Block ore = state.getBlock();
			int n = flood(level, player, origin, tool, s -> s.is(ore), 64);
			if (n > 0) Abilities.announce(player, "divine_vein");
		} else if (tool.is(ModItems.DIVINE_AXE.get()) && state.is(BlockTags.LOGS)) {
			int n = flood(level, player, origin, tool, s -> s.is(BlockTags.LOGS), 160);
			if (n > 0) Abilities.announce(player, "celestial_felling");
		}
	}

	private static int flood(ServerLevel level, Player player, BlockPos origin, ItemStack tool,
							 Predicate<BlockState> match, int limit) {
		Set<BlockPos> seen = new HashSet<>();
		Deque<BlockPos> queue = new ArrayDeque<>();
		seen.add(origin);
		queue.add(origin);
		int broken = 0;
		while (!queue.isEmpty() && broken < limit) {
			BlockPos p = queue.poll();
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					for (int dz = -1; dz <= 1; dz++) {
						if (broken >= limit) break;
						BlockPos n = p.offset(dx, dy, dz);
						if (!seen.add(n)) continue;
						BlockState s = level.getBlockState(n);
						if (!match.test(s)) continue;
						Block.dropResources(s, level, n, level.getBlockEntity(n), player, tool);
						level.destroyBlock(n, false, player);
						level.sendParticles(ParticleTypes.END_ROD, n.getX() + 0.5, n.getY() + 0.5, n.getZ() + 0.5, 3, 0.25, 0.25, 0.25, 0.02);
						queue.add(n);
						broken++;
					}
				}
			}
		}
		if (broken > 0) {
			tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
		}
		return broken;
	}
}
