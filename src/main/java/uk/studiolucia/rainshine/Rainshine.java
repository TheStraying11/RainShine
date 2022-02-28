package uk.studiolucia.rainshine;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rainshine extends JavaPlugin {
    Server server = getServer();
    ConsoleCommandSender logger = server.getConsoleSender();
    PluginManager pluginManager = server.getPluginManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginManager.registerEvents(new Events(), this);
        getCommand("rainshine").setExecutor(new Commands());
        logger.sendMessage(ChatColor.GREEN + "Rain or Shine! loaded.");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.sendMessage(ChatColor.RED + "Rain or Shine! unloaded");
    }
}
