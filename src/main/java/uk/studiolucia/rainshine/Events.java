package uk.studiolucia.rainshine;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.YELLOW + "Rain or Shine!: try /rainshine (or /rs) to vote on weather and time!");
    }
}