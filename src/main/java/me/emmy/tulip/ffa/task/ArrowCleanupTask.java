/*package me.emmy.tulip.ffa.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 14:53
 */
/*public class ArrowCleanupTask extends BukkitRunnable {

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            Collection<Arrow> arrows = world.getEntitiesByClass(Arrow.class);

            List<Arrow> arrowList = arrows.stream().collect(Collectors.toList());

            List<Arrow> stuckArrows = arrowList.stream()
                    .filter(this::isStuck)
                    .collect(Collectors.toList());

            for (Arrow arrow : stuckArrows) {
                arrow.remove();
            }
        }
    }


    private boolean isStuck(Arrow arrow) {
        Location location = arrow.getLocation();
        Block block = location.getBlock();

        boolean isEmbeddedInBlock = block.getType() != Material.AIR && block.getLocation().equals(location.getBlock().getLocation());

        boolean hasBeenAliveLongEnough = arrow.getTicksLived() > 100;

        return isEmbeddedInBlock && hasBeenAliveLongEnough;
    }
}
*/