package me.emmy.tulip.ffa.listener;

import me.emmy.tulip.Tulip;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 15:01
 */
public class ArrowCleanupListener implements Listener {

    private static final int ARROW_LIFETIME = 10;
    private static final double MIN_VELOCITY = 0.1;

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            Bukkit.getScheduler().runTaskLater(Tulip.getInstance(), () -> checkAndRemoveStuckArrow(arrow), ARROW_LIFETIME);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            Bukkit.getScheduler().runTaskLater(Tulip.getInstance(), () -> checkAndRemoveStuckArrow(arrow), ARROW_LIFETIME);
        }
    }

    private void checkAndRemoveStuckArrow(Arrow arrow) {
        if (arrow.isValid() && arrow.getVelocity().length() < MIN_VELOCITY) {
            arrow.remove();
        }
    }
}
