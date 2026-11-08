package com.divinitems.items;

import com.divinitems.Abilities;
import com.divinitems.DivineText;
import com.divinitems.Scheduler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Épée divine.
 *  - Tranchant céleste : chaque coup déclenche un croissant lumineux qui blesse les ennemis proches.
 *  - Exécution divine : un monstre sous 25% de vie est exécuté, et le joueur gagne de l'absorption.
 *  - Jugement dernier (clic droit) : marque la cible visée, puis la foudre céleste s'abat sur elle.
 */
public class DivineSwordItem extends SwordItem {
	public DivineSwordItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);
		if (!(attacker instanceof Player player) || !(attacker.level() instanceof ServerLevel sw)) {
			return result;
		}

		// --- Tranchant céleste ---
		Abilities.crescent(sw, attacker);
		sw.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0f, 1.5f);
		List<LivingEntity> around = sw.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(3.0),
			e -> e != attacker && e != target && e instanceof Enemy && e.isAlive());
		for (LivingEntity e : around) {
			e.hurt(sw.damageSources().playerAttack(player), 4.0f);
		}
		Abilities.announce(player, "celestial_edge");

		// --- Exécution divine ---
		if (target.isAlive() && target instanceof Mob && !(target instanceof EnderDragon)
			&& target.getHealth() <= target.getMaxHealth() * 0.25f) {
			Vec3 c = Abilities.center(target);
			target.hurt(sw.damageSources().playerAttack(player), Float.MAX_VALUE);
			Abilities.starBurst(sw, c);
			player.setAbsorptionAmount(Math.min(player.getAbsorptionAmount() + 4.0f, 20.0f));
			Abilities.announce(player, "divine_execution");
		}
		return result;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getCooldowns().isOnCooldown(this)) {
			return InteractionResultHolder.pass(stack);
		}
		LivingEntity target = Abilities.raycastLiving(player, 40.0);
		if (target == null) {
			return InteractionResultHolder.pass(stack);
		}
		if (level instanceof ServerLevel sw) {
			player.displayClientMessage(Component.translatable("message.divinitems.marked", target.getName()).withStyle(ChatFormatting.GOLD), true);
			target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, false, false, false));
			sw.playSound(null, player.blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.0f, 1.6f);

			// Anneaux de verrouillage qui se resserrent.
			for (int i = 0; i < 5; i++) {
				final double r = 2.2 - i * 0.4;
				Scheduler.later(i * 6, () -> {
					if (target.isAlive()) {
						Abilities.ring(sw, ParticleTypes.END_ROD, target.position().add(0, 0.1, 0), r, 20);
						Abilities.ring(sw, Abilities.GOLD_DUST, target.position().add(0, target.getBbHeight(), 0), r, 14);
					}
				});
			}

			// Frappe après 1,5 s.
			Scheduler.later(30, () -> {
				if (!target.isAlive() || !(target.level() instanceof ServerLevel tw)) return;
				Vec3 base = target.position();
				Abilities.pillar(tw, base, 40.0);
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(tw);
				if (bolt != null) {
					bolt.moveTo(base);
					bolt.setVisualOnly(true);
					tw.addFreshEntity(bolt);
				}
				tw.playSound(null, target.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 2.0f, 1.2f);
				Abilities.starBurst(tw, Abilities.center(target));
				target.hurt(tw.damageSources().indirectMagic(player, player), 40.0f);
			});
			Abilities.announce(player, "last_judgment");
		}
		player.getCooldowns().addCooldown(this, 160);
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(DivineText.levelLine());
		DivineText.abilityLines(tooltip, "celestial_edge", "divine_execution", "last_judgment");
	}
}
