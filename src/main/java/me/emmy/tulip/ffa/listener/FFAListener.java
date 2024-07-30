package me.emmy.tulip.ffa.listener;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.cooldown.Cooldown;
import me.emmy.tulip.cooldown.CooldownRepository;
import me.emmy.tulip.ffa.safezone.FFASpawnHandler;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.utils.CC;
import me.emmy.tulip.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Emmy
 * @project Tulip
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            event.setDeathMessage(null);

            List<Item> droppedItems = new ArrayList<>();
            for (ItemStack drop : event.getDrops()) {
                if (drop != null && drop.getType() != Material.AIR) {
                    droppedItems.add(player.getWorld().dropItemNaturally(player.getLocation(), drop));
                }
            }
            event.getDrops().clear();

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Item item : droppedItems) {
                        if (item != null && item.isValid()) {
                            item.remove();
                        }
                    }
                }
            }.runTaskLater(Tulip.getInstance(), 100L);

            Player killer = PlayerUtil.getLastAttacker(player);
            if (killer != null) {
                player.sendMessage(CC.translate("&cYou have been killed by &4" + killer.getName() + "&c."));
                killer.sendMessage(CC.translate("&aYou have killed &2" + player.getName() + "&a."));
            }

            Tulip.getInstance().getServer().getScheduler().runTaskLater(Tulip.getInstance(), () -> player.spigot().respawn(), 1L);

            Bukkit.getScheduler().runTaskLater(Tulip.getInstance(), () -> {
                profile.getFfaMatch().handleDeath(player, killer);
            }, 1L);
        }
    }


    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            profile.getFfaMatch().leave(player);
        }
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            profile.getFfaMatch().leave(player);
        }
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(event.getEntity().getUniqueId());
            if (profile.getState() == EnumProfileState.FFA) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                PlayerUtil.setLastAttacker(player, damager);
            }
        }
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        FFASpawnHandler ffaSpawnHandler = Tulip.getInstance().getFfaSpawnHandler();
        if (ffaSpawnHandler.getCuboid().isIn((victim)) && ffaSpawnHandler.getCuboid().isIn((attacker)) ||
                !ffaSpawnHandler.getCuboid().isIn(victim) && ffaSpawnHandler.getCuboid().isIn(attacker) ||
                ffaSpawnHandler.getCuboid().isIn(victim) && !ffaSpawnHandler.getCuboid().isIn(attacker)) {

            event.setCancelled(true);
        }

        /*CombatManager combatManager = Tulip.getInstance().getCombatManager();

        Bukkit.getPluginManager().callEvent(new CombatTagEvent(victim, attacker));
        combatManager.setCombatTime(victim, 16);
        combatManager.setCombatTime(attacker, 16);
        combatManager.setCombatSet(victim, true);
        combatManager.setCombatSet(attacker, true);*/
    }

    /**
     * Handles the BlockPlaceEvent. The event is cancelled if the player is in the FFA state and tries to place a block.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the BlockBreakEvent. The event is cancelled if the player is in the FFA state and tries to break a block.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the PlayerInteractEvent. The event is cancelled if the player is in the FFA state and tries to interact with an item.
     *
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = event.getItem();

        if (profile.getState() == EnumProfileState.FFA && item != null && item.getType() == Material.ENDER_PEARL) {
            if (event.getAction().name().contains("RIGHT_CLICK")) {
                if (Tulip.getInstance().getFfaSpawnHandler().getCuboid().isIn(player)) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
                    return;
                }

                CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));

                if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before using another ender pearl."));
                    return;
                }

                Cooldown cooldown = optionalCooldown.orElseGet(() -> {
                    Cooldown newCooldown = new Cooldown(15 * 1000L, () -> player.sendMessage(CC.translate("&aYou can now use pearls again!")));
                    cooldownRepository.addCooldown(player.getUniqueId(), "ENDERPEARL", newCooldown);
                    return newCooldown;
                });

                cooldown.resetCooldown();
            }
        }
    }

}