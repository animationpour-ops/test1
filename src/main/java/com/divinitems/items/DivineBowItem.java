package com.divinitems.items;

import com.divinitems.Abilities;
import com.divinitems.DivineText;
import com.divinitems.Scheduler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

/**
 * Arc divin. À pleine charge :
 *  - Constellation meurtrière : des lignes d'étoiles relient les monstres proches et les frappent.
 *  - Pluie d'étoiles (en étant accroupi) : une pluie d'étoiles s'abat sur la zone visée.
 */
public class DivineBowItem extends BowItem {
	public DivineBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
		super.releaseUsing(stack, level, user, timeLeft);
		if (!(level instanceof ServerLevel sw) || !(user instanceof ServerPlayer player)) return;

		int used = 72000 - timeLeft;
		if (BowItem.getPowerForTime(used) < 0.95f) return;
		if (player.getCooldowns().isOnCooldown(this)) return;

		boolean fired = player.isShiftKeyDown() ? starRain(sw, player) : constellation(sw, player);
		if (fired) {
			player.getCooldowns().addCooldown(this, 40);
		}
	}

	private boolean constellation(ServerLevel sw, ServerPlayer player) {
		List<LivingEntity> targets = sw.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(32.0),
			e -> e instanceof Enemy && e.isAlive());
		if (targets.isEmpty()) return false;
		targets.sort(Comparator.comparingDouble(e -> e.distanceToSqr(player)));

		Vec3 prev = player.getEyePosition().subtract(0, 0.3, 0);
		int count = Math.min(6, targets.size());
		for (int i = 0; i < count; i++) {
			LivingEntity t = targets.get(i);
			Vec3 pos = Abilities.center(t);
			Abilities.line(sw, ParticleTypes.END_ROD, prev, pos, 0.4);
			Abilities.line(sw, Abilities.GOLD_DUST, prev, pos, 0.9);
			Abilities.starBurst(sw, pos);
			t.hurt(sw.damageSources().indirectMagic(player, player), 10.0f);
			prev = pos;
		}
		sw.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 2.0f, 1.8f);
		Abilities.announce(player, "deadly_constellation");
		return true;
	}

	private boolean starRain(ServerLevel sw, ServerPlayer player) {
		HitResult hit = player.pick(60.0, 1.0f, false);
		Vec3 center = hit.getLocation();
		for (int i = 0; i < 18; i++) {
			double angle = sw.getRandom().nextDouble() * Math.PI * 2;
			double dist = Math.sqrt(sw.getRandom().nextDouble()) * 5.0;
			Vec3 p = center.add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
			Scheduler.later(i * 3, () -> {
				for (double y = 14; y >= 0; y -= 0.7) {
					sw.sendParticles(ParticleTypes.END_ROD, p.x, p.y + y, p.z, 1, 0.03, 0.0, 0.03, 0.0);
				}
				Abilities.starBurst(sw, p.add(0, 0.2, 0));
				sw.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 0.6f, 1.6f);
				List<LivingEntity> hits = sw.getEntitiesOfClass(LivingEntity.class, new AABB(p, p).inflate(1.8, 2.0, 1.8),
					e -> e instanceof Enemy && e.isAlive());
				for (LivingEntity e : hits) {
					e.hurt(sw.damageSources().indirectMagic(player, player), 6.0f);
				}
			});
		}
		Abilities.announce(player, "star_rain");
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(DivineText.levelLine());
		DivineText.abilityLines(tooltip, "deadly_constellation", "star_rain");
	}
}
