package yusama125718.man10missile;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static yusama125718.man10missile.Man10Missile.*;

public class Runnable extends BukkitRunnable {

    private final UUID p;
    private final MissilePlayer m;

    public Runnable(UUID P){
        p = P;
        m = players.get(p);
    }

    @Override
    public void run() {
        if (!system || m.finish || Bukkit.getPlayer(p) == null){
            if (Bukkit.getPlayer(p) != null) Finish();
            players.remove(p);
            this.cancel();
            return;
        }
        if (!(Bukkit.getPlayer(p).getLocation().add(0, -1, 0).getBlock().getType().equals(Material.AIR))){
            Finish();
            this.cancel();
            return;
        }
        for (String c : m.profile.runnable){
            String command = c.replace("<player>", Bukkit.getPlayer(p).getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        Location loc = Bukkit.getPlayer(p).getLocation();
        if (m.profile.amount != 0) Bukkit.getPlayer(p).getLocation().getWorld().spawnParticle(m.profile.particle, Bukkit.getPlayer(p).getLocation(),m.profile.amount,0,2,0);
        Bukkit.getPlayer(p).setVelocity(loc.getDirection().multiply(m.profile.vector));
        Bukkit.getPlayer(p).sendActionBar(Component.text("経過時間：" + String.format("%.1f", m.time) + " / " + m.profile.time));
        m.time += period / 20.0;
        if (m.time >= players.get(p).profile.time){
            Finish();
            this.cancel();
        }
    }

    private void Finish(){
        for (String c : m.profile.command){
            if (c.equals("bomb")) {
                if (m.profile.bomb == null) continue;
                Explosion.Bomb(Bukkit.getPlayer(p), m.profile.bomb);
                continue;
            }
            String command = c.replace("<player>", Bukkit.getPlayer(p).getName());
            Bukkit.getPlayer(p).performCommand(command);
        }
        for (String c : m.profile.console_command){
            if (c.equals("bomb")) {
                if (m.profile.bomb == null) continue;
                Explosion.Bomb(Bukkit.getPlayer(p), m.profile.bomb);
                continue;
            }
            String command = c.replace("<player>", Bukkit.getPlayer(p).getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        if (m.profile.head != null) Bukkit.getPlayer(p).removePotionEffect(PotionEffectType.INVISIBILITY);
        Bukkit.getPlayer(p).getInventory().clear();
        Bukkit.getPlayer(p).getInventory().setContents(m.inv);
        if (m.invisibility != 0) Bukkit.getPlayer(p).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY ,m.invisibility ,1));
        Bukkit.getPlayer(p).teleport(players.get(p).startloc);
        Bukkit.getPlayer(p).setAllowFlight(false);
        Bukkit.getPlayer(p).setFallDistance(0F);
        Bukkit.getPlayer(p).sendActionBar(Component.text(""));
        players.remove(p);
    }
}
