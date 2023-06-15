package yusama125718.man10missile;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

import static yusama125718.man10missile.Man10Missile.*;

public class Runnable extends BukkitRunnable {

    private final UUID p;

    public Runnable(UUID P){
        p = P;
    }

    @Override
    public void run() {
        if (!system || players.get(p).finish){
            if (Bukkit.getPlayer(p) != null) Finish();
            new RemovePlayer(p).runTaskLater(mmissile, 2);
            this.cancel();
        }
        if (!(Bukkit.getPlayer(p).getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR))){
            Finish();
            this.cancel();
        }
        Location loc = Bukkit.getPlayer(p).getLocation();
        Bukkit.getPlayer(p).setVelocity(loc.getDirection().multiply(players.get(p).profile.vector));
        Bukkit.getPlayer(p).sendActionBar(Component.text("経過時間：" + String.format("%.1f", players.get(p).time) + " / " + players.get(p).profile.time));
        players.get(p).time += period / 20.0;
        if (players.get(p).time >= players.get(p).profile.time){
            Finish();
            this.cancel();
        }
    }

    private void Finish(){
        for (String c : players.get(p).profile.command){
            String command = c.replace("<player>", Bukkit.getPlayer(p).getName());
            Bukkit.getPlayer(p).performCommand(command);
        }
        for (String c : players.get(p).profile.console_command){
            String command = c.replace("<player>", Bukkit.getPlayer(p).getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        Bukkit.getPlayer(p).sendActionBar(Component.text(""));
        Bukkit.getPlayer(p).teleport(players.get(p).startloc);
        new RemovePlayer(p).runTaskLater(mmissile, 2);
    }
}
