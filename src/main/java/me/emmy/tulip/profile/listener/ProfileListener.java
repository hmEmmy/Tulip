package me.emmy.tulip.profile.listener;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigHandler;
import me.emmy.tulip.hotbar.HotbarUtility;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.profile.ProfileRepository;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * @author Emmy
 * @project Tulip
 * @date 27/07/2024 - 18:38
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(player.getUniqueId());
        profile.loadProfile();

        ProfileRepository profileRepository = Tulip.getInstance().getProfileRepository();
        profileRepository.addProfile(profile.getUuid(), profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ProfileRepository profileRepository = Tulip.getInstance().getProfileRepository();
        Profile profile = profileRepository.getProfile(player.getUniqueId());
        profile.setName(player.getName());
        profile.setOnline(true);
        profile.getKitLayout().setEditing(false);
        profile.setState(EnumProfileState.SPAWN);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        Tulip.getInstance().getSpawnHandler().teleportToSpawn(player);
        player.getInventory().setHeldItemSlot(0);
        HotbarUtility.applyHotbarItems(player);

        sendWelcomeMessage(player);

        event.setJoinMessage(null);
    }


    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setOnline(false);
        profile.getKitLayout().setEditing(false);
        profile.saveProfile();
        event.setQuitMessage(null);
    }

    /**
     * Send the welcome message to the player.
     *
     * @param player the player
     */
    private void sendWelcomeMessage(Player player) {
        List<String> messages = ConfigHandler.getInstance().getLocaleConfig().getStringList("welcome-message");
        for (String message : messages) {
            player.sendMessage(CC.translate(message).replace("{player}", player.getName()));
        }
    }
}
