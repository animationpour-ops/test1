package com.divinitems;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/** Événements Forge (bus principal) : tâches différées, effets d'armure, vein miner, chute. */
@Mod.EventBusSubscriber(modid = DivineItems.MOD_ID)
public final class ModEvents {
	private ModEvents() {}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		Scheduler.tick();
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) return;
		int t = server.getTickCount();
		if (t % 10 == 0) {
			for (ServerPlayer p : server.getPlayerList().getPlayers()) {
				ArmorEffects.tick(p, t);
			}
		}
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		Scheduler.clear();
		ArmorEffects.clear();
	}

	/** Veine divine / Abattage céleste. */
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (event.isCanceled()) return;
		if (event.getLevel() instanceof ServerLevel level) {
			VeinMiner.onBlockBreak(level, event.getPlayer(), event.getPos(), event.getState());
		}
	}

	/** Pas divin : aucun dégât de chute avec les bottes. */
	@SubscribeEvent
	public static void onFall(LivingFallEvent event) {
		if (event.getEntity() instanceof Player p
			&& p.getItemBySlot(EquipmentSlot.FEET).is(ModItems.DIVINE_BOOTS.get())) {
			event.setCanceled(true);
		}
	}
}
