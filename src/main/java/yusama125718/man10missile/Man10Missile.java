package yusama125718.man10missile;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

        public MissilePlayer(Missile PROFILE, Location LOC){
            time = (double) 0;
            profile = PROFILE;
            finish = false;
            startloc = LOC;
        }
    }

    public static class Missile{
        public String name;
        public Double time;
        public Double vector;
        public List<String> command;
        public List<String> start_command;
        public List<String> start_c_command;
        public List<String> console_command;

        public Missile(String NAME, Double TIME, Double VECTOR, List<String> COMMAND, List<String> S_COMMAND, List<String> SC_COMMAND, List<String> C_COMMAND){
            name = NAME;
            time = TIME;
            vector = VECTOR;
            command = COMMAND;
            start_command = S_COMMAND;
            start_c_command = SC_COMMAND;
            console_command = C_COMMAND;
        }
    }

    @EventHandler
    public static void PlayerQuitEvent(PlayerQuitEvent e){
        if (!players.containsKey(e.getPlayer().getUniqueId())) return;
        players.get(e.getPlayer().getUniqueId()).finish = true;
    }
}
