
package uk.studiolucia.rainshine;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Objects;

public class Commands implements CommandExecutor {
    double voteThreshold = 0.50001;
    String Vote = "";
    int Votes = -1;
    ArrayList<CommandSender> Voters = new ArrayList<CommandSender>();

    int expire = -1;

    BukkitScheduler scheduler = Bukkit.getScheduler();
    Server server = Bukkit.getServer();

    private void resetVotes() {
        Vote = "";
        Votes = 0;
        Voters.clear();
    }

    private void votePassed() {
        resetVotes();
        server.broadcastMessage(ChatColor.GREEN + "Vote passed! performing command: " + Vote);
        server.dispatchCommand(Bukkit.getServer().getConsoleSender(), Vote);
    }

    private void voteFailed() {
        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Vote Failed");
    }

    private void voteExpired() {
        if (Votes >= Voters.size()*voteThreshold) {
            votePassed();
        }
        else {
            voteFailed();
        }
    }

    private void startVote(String vote, CommandSender sender) {
        expire = scheduler.scheduleSyncDelayedTask(
                Objects.requireNonNull(server.getPluginManager().getPlugin("Rainshine")),
                this::voteExpired,
                1200
        );
        Votes = 1;
        Voters.add(sender);
        Vote = vote;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equals("rainshine")) {
            if (Voters.contains(sender)) {
                sender.sendMessage(ChatColor.RED + "You have already voted on this issue, please wait until it expires to try again.");
                return true;
            }
            if (Vote.equals("")) {
                if (args.length == 1) {
                    if (args[0].equals("day")) {
                        startVote("/time set day", sender);
                        server.broadcastMessage(sender.getName() + " Wants to change the time to day! vote now, you have 60s");
                    }
                    else if (args[0].equals("night")) {
                        startVote("/time set night", sender);
                        server.broadcastMessage(sender.getName() + " Wants to change the time to night! vote now, you have 60s");
                    }
                    else if (args[0].equals("clear")) {
                        startVote("/weather clear", sender);
                        server.broadcastMessage(sender.getName() + " Wants to change the weather to clear! vote now, you have 60s");
                    }
                    else if (args[0].equals("rain")) {
                        startVote("/weather rain", sender);
                        server.broadcastMessage(sender.getName() + " Wants to change the weather to rain! vote now, you have 60s");
                    }
                    else if (args[0].equals("storm")) {
                        startVote("/weather thunder", sender);
                        server.broadcastMessage(sender.getName() + " Wants to change the weather to storm! vote now, you have 60s");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Usage: /rainshine <day, night, clear, rain, storm>");
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Usage: /rainshine <day, night, clear, rain, storm>");
                }
            }
            else {
                if (args.length == 1) {
                    if (args[0].equals("accept")) {
                        Votes++;
                        Voters.add(sender);
                    }
                    else if (args[0].equals("deny")) {
                        Votes--;
                        Voters.add(sender);
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Usage: /rainshine <accept, deny>");
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Usage: /rainshine <accept, deny>");
                }
            }

            if (Votes >= (server.getOnlinePlayers().toArray().length * voteThreshold)) {
                votePassed();
                if (expire != -1) {
                    scheduler.cancelTask(expire);
                }
            }
        }

        return true;
    }
}
