package com.divinitems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Effets passifs de l'armure divine (appelé toutes les 10 ticks). */
public final class ArmorEffects {
	private ArmorEffects() {}

	/** Dernière cible du Regard autoritaire par joueur (pour ne pas spammer la barre d'action). */
	private static final Map<UUID, UUID> LAST_GAZE = new HashMap<>();

	public static void tick(ServerPlayer p, int serverTick) {
		boolean head = p.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.DIVINE_HELMET.get());
		boolean chest = p.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.DIVINE_CHESTPLATE.get());
		boolean legs = p.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.DIVINE_LEGGINGS.get());
		boolean feet = p.getItemBySlot(EquipmentSlot.FEET).is(ModItems.DIVINE_BOOTS.get());

		if (head) {
			// Vision nocturne
			p.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 420, 0, true, false, true));
			gaze(p);
		} else {
			LAST_GAZE.remove(p.getUUID());
		}

		if (chest) {
			// Présence céleste : régénération + résistance, bouclier doré rechargé toutes les 5 s.
			p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, true, false, true));
			p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0, true, false, true));
			if (serverTick % 100 == 0) {
				p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 2, true, false, true));
			}
		}

		if (legs) {
			// Course céleste
			p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1, true, false, true));
		}

		if (feet) {
			// Pas divin : saut amélioré (la chute est annulée dans ModEvents).
			p.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 2, true, false, true));
			if (!p.onGround() || p.isSprinting()) {
				ServerLevel w = p.serverLevel();
				w.sendParticles(ParticleTypes.END_ROD, p.getX(), p.getY() + 0.1, p.getZ(), 4, 0.25, 0.05, 0.25, 0.01);
				w.sendParticles(Abilities.GOLD_DUST, p.getX(), p.getY() + 0.1, p.getZ(), 2, 0.25, 0.05, 0.25, 0.0);
			}
		}
	}

	/** Regard autoritaire : le monstre que le joueur fixe est paralysé et révélé. */
	private static void gaze(ServerPlayer p) {
		LivingEntity t = Abilities.raycastLiving(p, 24.0);
		if (t instanceof Enemy) {
			t.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 4, false, false, false));
			t.addEffect(new MobEffectInstance(MobEffects.GLOWING, 30, 0, false, false, false));
			p.serverLevel().sendParticles(ParticleTypes.END_ROD, t.getX(), t.getEyeY() + 0.4, t.getZ(), 3, 0.2, 0.1, 0.2, 0.01);
			UUID last = LAST_GAZE.get(p.getUUID());
			if (last == null || !last.equals(t.getUUID())) {
				LAST_GAZE.put(p.getUUID(), t.getUUID());
				Abilities.announce(p, "authoritative_gaze");
			}
		} else {
			LAST_GAZE.remove(p.getUUID());
		}
	}

	public static void clear() {
		LAST_GAZE.clear();
	}
}
