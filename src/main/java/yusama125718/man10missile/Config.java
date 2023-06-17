package yusama125718.man10missile;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static yusama125718.man10missile.Man10Missile.*;

public class Config {
    private static final File folder = new File(mmissile.getDataFolder().getAbsolutePath() + File.separator + "mmissiles");
    private static final File bombfolder = new File(mmissile.getDataFolder().getAbsolutePath() + File.separator + "bomb");
    private static File configfile;
    private static File bombfile;

    public static void LoadBombFile(){
        if (mmissile.getDataFolder().listFiles() != null){
            for (File file : Objects.requireNonNull(mmissile.getDataFolder().listFiles())) {
                if (file.getName().equals("bomb")) {
                    bombfile = file;
                    return;
                }
            }
        }
        if (bombfolder.mkdir()) {
            Bukkit.broadcast(Component.text(prefix + "§rステージフォルダを作成しました"), "mmissile.op");
            bombfile = bombfolder;
        } else {
            Bukkit.broadcast(Component.text(prefix + "§rステージフォルダの作成に失敗しました"), "mmissile.op");
        }
    }

    public static void LoadFile(){
        if (mmissile.getDataFolder().listFiles() != null){
            for (File file : Objects.requireNonNull(mmissile.getDataFolder().listFiles())) {
                if (file.getName().equals("mmissiles")) {
                    configfile = file;
                    return;
                }
            }
        }
        if (folder.mkdir()) {
            Bukkit.broadcast(Component.text(prefix + "§rステージフォルダを作成しました"), "mmissile.op");
            configfile = folder;
        } else {
            Bukkit.broadcast(Component.text(prefix + "§rステージフォルダの作成に失敗しました"), "mmissile.op");
        }
    }

    public static void LoadBombYaml(){
        if (bombfile.listFiles() == null) return;
        for (File file : bombfile.listFiles()){
            YamlConfiguration yml =  YamlConfiguration.loadConfiguration(file);
            if (yml.get("name") == null || yml.get("radius") == null || yml.get("damage") == null) continue;
            String name = yml.getString("name");
            Double damage = yml.getDouble("damage");
            Double radius = yml.getDouble("radius");
            boolean armor = true;
            if (yml.get("armor") != null) armor = yml.getBoolean("armor");
            boolean team = false;
            if (yml.get("team") != null) team = yml.getBoolean("team");
            bombs.add(new Explosion.Settings(name, armor, team, radius, damage));
        }
    }

    public static void LoadYaml(){
        if (configfile.listFiles() == null) return;
        for (File file : configfile.listFiles()){
            YamlConfiguration yml =  YamlConfiguration.loadConfiguration(file);
            if (yml.get("name") == null || yml.get("time") == null || yml.get("vector") == null) continue;
            String name = yml.getString("name");
            Double time = yml.getDouble("time");
            Double vector = yml.getDouble("vector");
            List<String> command = new ArrayList<>();
            List<String> S_command = new ArrayList<>();
            List<String> SC_command = new ArrayList<>();
            List<String> C_command = new ArrayList<>();
            List<String> runnable = new ArrayList<>();
            int amount = 0;
            Particle particle = Particle.LAVA;
            ItemStack head = null;
            Explosion.Settings bomb = null;
            int down = 0;
            if (yml.get("command") != null) command = yml.getStringList("command");
            if (yml.get("S_command") != null) S_command = yml.getStringList("S_command");
            if (yml.get("SC_command") != null) SC_command = yml.getStringList("SC_command");
            if (yml.get("C_command") != null) C_command = yml.getStringList("C_command");
            if (yml.get("runnable") != null) runnable = yml.getStringList("runnable");
            if (yml.get("particle.particle") != null) {
                String p = yml.getString("particle.particle");
                particle = Particle.valueOf(p);
            }
            if (yml.get("head.material") != null) {
                head = new ItemStack(Material.valueOf(yml.getString("head.material")));
                if (yml.get("head.cmd") != null) {
                    ItemMeta meta = head.getItemMeta();
                    meta.setCustomModelData(yml.getInt("head.cmd"));
                    head.setItemMeta(meta);
                }
            }
            if (yml.get("particle.amount") != null) amount = yml.getInt("particle.amount");
            if (yml.get("bomb") != null) {
                String bomb_name = yml.getString("bomb");
                for (Explosion.Settings s : bombs) if (s.name.equals(bomb_name)) bomb = s;
            }
            if (yml.get("downmode") != null) down = yml.getInt("downmode");
            missiles.add(new Missile(name, time, vector, command, S_command, SC_command, C_command, runnable, particle, amount, head, bomb, down));
        }
    }
}
