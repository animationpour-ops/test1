package com.divinitems;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/** Outils partagés : ciblage et effets visuels (particules). */
public final class Abilities {
	private Abilities() {}

	public static final ParticleOptions GOLD_DUST = new DustParticleOptions(new Vector3f(1.0f, 0.82f, 0.25f), 1.4f);
	public static final ParticleOptions WHITE_DUST = new DustParticleOptions(new Vector3f(1.0f, 1.0f, 1.0f), 1.2f);

	/** Entité vivante visée par le joueur (portée en blocs), ou null. */
	public static LivingEntity raycastLiving(LivingEntity user, double range) {
		Vec3 eye = user.getEyePosition();
		Vec3 look = user.getViewVector(1.0f);
		Vec3 end = eye.add(look.scale(range));
		AABB box = user.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0);
		EntityHitResult hit = ProjectileUtil.getEntityHitResult(user, eye, end, box,
			e -> e instanceof LivingEntity && e.isAlive() && !e.isSpectator() && e != user, range * range);
		return hit == null ? null : (LivingEntity) hit.getEntity();
	}

	public static void announce(Player player, String abilityId) {
		player.displayClientMessage(DivineText.actionBar(abilityId), true);
	}

	public static Vec3 center(Entity e) {
		return e.position().add(0, e.getBbHeight() / 2.0, 0);
	}

	/** Ligne de particules entre deux points. */
	public static void line(ServerLevel w, ParticleOptions p, Vec3 a, Vec3 b, double step) {
		Vec3 d = b.subtract(a);
		double len = d.length();
		if (len < 1e-4) return;
		Vec3 dir = d.scale(1.0 / len);
		for (double t = 0; t <= len; t += step) {
			Vec3 pt = a.add(dir.scale(t));
			w.sendParticles(p, pt.x, pt.y, pt.z, 1, 0, 0, 0, 0);
		}
	}

	/** Anneau horizontal de particules. */
	public static void ring(ServerLevel w, ParticleOptions p, Vec3 c, double r, int n) {
		for (int i = 0; i < n; i++) {
			double a = (Math.PI * 2.0 * i) / n;
			w.sendParticles(p, c.x + Math.cos(a) * r, c.y, c.z + Math.sin(a) * r, 1, 0, 0, 0, 0);
		}
	}

	/** Colonne de lumière verticale. */
	public static void pillar(ServerLevel w, Vec3 base, double height) {
		for (double y = 0; y <= height; y += 0.5) {
			w.sendParticles(ParticleTypes.END_ROD, base.x, base.y + y, base.z, 1, 0.12, 0, 0.12, 0.0);
			if (((int) (y * 2)) % 4 == 0) {
				w.sendParticles(GOLD_DUST, base.x, base.y + y, base.z, 1, 0.3, 0, 0.3, 0.0);
			}
		}
	}

	/** Croissant lumineux devant l'attaquant (Tranchant céleste). */
	public static void crescent(ServerLevel w, LivingEntity attacker) {
		Vec3 eye = attacker.getEyePosition().subtract(0, 0.4, 0);
		double yaw = Math.toRadians(attacker.getYRot());
		for (int i = -10; i <= 10; i++) {
			double phi = yaw + Math.toRadians(i * 8.0);
			double r = 2.3 - Math.abs(i) * 0.04;
			double x = eye.x + (-Math.sin(phi)) * r;
			double z = eye.z + Math.cos(phi) * r;
			double y = eye.y + 0.35 - Math.abs(i) * 0.03;
			w.sendParticles(ParticleTypes.END_ROD, x, y, z, 1, 0, 0, 0, 0.01);
			w.sendParticles(WHITE_DUST, x, y, z, 1, 0.02, 0.02, 0.02, 0.0);
		}
	}

	/** Éclat d'étoile sur une cible (Constellation meurtrière / Pluie d'étoiles). */
	public static void starBurst(ServerLevel w, Vec3 c) {
		w.sendParticles(ParticleTypes.FLASH, c.x, c.y, c.z, 1, 0, 0, 0, 0);
		ring(w, ParticleTypes.END_ROD, c, 0.9, 12);
		for (int i = 0; i < 8; i++) {
			double a = Math.PI * 2 * i / 8;
			Vec3 tip = c.add(Math.cos(a) * 1.4, 0.0, Math.sin(a) * 1.4);
			line(w, ParticleTypes.END_ROD, c, tip, 0.35);
		}
	}
}
