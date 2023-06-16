package yusama125718.man10missile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public final class Man10Missile extends JavaPlugin implements EventListener {

    public static JavaPlugin mmissile;
    public static List<Missile> missiles = new ArrayList<>();
    public static HashMap<UUID,MissilePlayer> players = new HashMap<>();
    public static Boolean system;
    public static String prefix;
    public static File configfile;
    public static Integer period;

    @Override
    public void onEnable() {
        mmissile = this;
        mmissile.saveDefaultConfig();
        system = mmissile.getConfig().getBoolean("system");
        prefix = mmissile.getConfig().getString("prefix");
        period = mmissile.getConfig().getInt("period");
        Config.LoadFile();
        Config.LoadYaml();
        getCommand("mmissile").setExecutor(new Command());
    }


    public static class MissilePlayer{
        public Missile profile;
        public Double time;
        public Boolean finish;
        public Location startloc;
        public ItemStack head;
        public Integer invisibility;

        public MissilePlayer(Missile PROFILE, Location LOC, ItemStack HEAD, Integer INVISIBILITY){
            time = (double) 0;
            profile = PROFILE;
            finish = false;
            startloc = LOC;
            head = HEAD;
            invisibility = INVISIBILITY;
        }
    }

    public static class Missile{
        public String name;
        public Double time;
        public Double vector;
        public Particle particle;
        public Integer amount;
        public List<String> command;
        public List<String> start_command;
        public List<String> runnable;
        public List<String> start_c_command;
        public List<String> console_command;
        public ItemStack head;

        public Missile(String NAME, Double TIME, Double VECTOR, List<String> COMMAND, List<String> S_COMMAND, List<String> SC_COMMAND, List<String> C_COMMAND, List<String> RUNNABLE, Particle Particle, Integer Amount, ItemStack HEAD){
            name = NAME;
            time = TIME;
            vector = VECTOR;
            command = COMMAND;
            start_command = S_COMMAND;
            start_c_command = SC_COMMAND;
            console_command = C_COMMAND;
            runnable = RUNNABLE;
            particle = Particle;
            amount = Amount;
            head = HEAD;
        }
    }

    @EventHandler
    public static void PlayerQuitEvent(PlayerQuitEvent e){
        if (!players.containsKey(e.getPlayer().getUniqueId())) return;
        players.get(e.getPlayer().getUniqueId()).finish = true;
        if (players.get(e.getPlayer().getUniqueId()).profile.head != null) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1, 1));
        if (players.get(e.getPlayer().getUniqueId()).head != null) e.getPlayer().getInventory().setHelmet(players.get(e.getPlayer().getUniqueId()).head.clone());
        else e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
    }
}
