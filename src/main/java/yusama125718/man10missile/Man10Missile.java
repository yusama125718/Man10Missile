package yusama125718.man10missile;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Man10Missile extends JavaPlugin implements EventListener, @NotNull Listener {

    public static JavaPlugin mmissile;
    public static List<Missile> missiles = new ArrayList<>();
    public static List<Explosion.Settings> bombs = new ArrayList<>();
    public static HashMap<UUID,MissilePlayer> players = new HashMap<>();
    public static Boolean system;
    public static String prefix;
    public static Integer period;

    @Override
    public void onEnable() {
        mmissile = this;
        getServer().getPluginManager().registerEvents(this, this);
        mmissile.saveDefaultConfig();
        system = mmissile.getConfig().getBoolean("system");
        prefix = mmissile.getConfig().getString("prefix");
        period = mmissile.getConfig().getInt("period");
        Config.LoadBombFile();
        Config.LoadBombYaml();
        Config.LoadFile();
        Config.LoadYaml();
        getCommand("mmissile").setExecutor(new Command());
    }

    public static class MissilePlayer{
        public Missile profile;
        public Double time;
        public Boolean finish;
        public Location startloc;
        public ItemStack[] inv;
        public Integer invisibility;

        public MissilePlayer(Missile PROFILE, Location LOC, ItemStack[] INV, Integer INVISIBILITY){
            time = (double) 0;
            profile = PROFILE;
            finish = false;
            startloc = LOC;
            inv = INV;
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
        public Explosion.Settings bomb;
        public Integer downmode;

        public Missile(String NAME, Double TIME, Double VECTOR, List<String> COMMAND, List<String> S_COMMAND, List<String> SC_COMMAND, List<String> C_COMMAND, List<String> RUNNABLE, Particle Particle, Integer Amount, ItemStack HEAD, Explosion.Settings Bomb, Integer Down){
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
            bomb = Bomb;
            downmode = Down;
        }
    }

    @EventHandler
    public static void PlayerQuitEvent(PlayerQuitEvent e){
        if (!players.containsKey(e.getPlayer().getUniqueId())) return;
        players.get(e.getPlayer().getUniqueId()).finish = true;
        if (players.get(e.getPlayer().getUniqueId()).profile.head != null) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1, 1));
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setContents(players.get(e.getPlayer().getUniqueId()).inv);
    }

    @EventHandler
    public static void InventoryClick(InventoryClickEvent e){
        if (players.containsKey(e.getWhoClicked().getUniqueId())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public static void PlayerDeathEvent(PlayerDeathEvent e){
        if (!players.containsKey(e.getPlayer().getUniqueId())) return;
        players.remove(e.getPlayer().getUniqueId());
        players.get(e.getPlayer().getUniqueId()).finish = true;
        if (players.get(e.getPlayer().getUniqueId()).profile.head != null) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1, 1));
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setContents(players.get(e.getPlayer().getUniqueId()).inv);
        e.setCancelled(true);
    }
}
