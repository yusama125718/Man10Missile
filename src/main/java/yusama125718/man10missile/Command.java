package yusama125718.man10missile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import static yusama125718.man10missile.Man10Missile.*;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("mmissile.p")) return true;
        switch (args.length){
            case 1:
                if (args[0].equals("on") && sender.hasPermission("mmissile.op")){
                    system = true;
                    mmissile.getConfig().set("system", system);
                    mmissile.saveConfig();
                    sender.sendMessage(prefix+"ONにしました");
                    return true;
                }
                else if (args[0].equals("off") && sender.hasPermission("mmissile.op")){
                    system = false;
                    mmissile.getConfig().set("system", system);
                    mmissile.saveConfig();
                    sender.sendMessage(prefix+"OFFにしました");
                    return true;
                }
                else if (args[0].equals("reload") && sender.hasPermission("mmissile.op")){
                    system = false;
                    missiles.clear();
                    bombs.clear();
                    players.clear();
                    system = mmissile.getConfig().getBoolean("system");
                    prefix = mmissile.getConfig().getString("prefix");
                    period = mmissile.getConfig().getInt("period");
                    Config.LoadBombYaml();
                    Config.LoadBombYaml();
                    Config.LoadFile();
                    Config.LoadYaml();
                    sender.sendMessage(prefix+"リロードしました");
                    return true;
                }
                else if (args[0].equals("help")){
                    sender.sendMessage(prefix + "§7/mmissile start [ミサイル名] §r: ミサイルを発射します");
                    sender.sendMessage(prefix + "§7/mmissile list §r: ミサイルのリストを表示します");
                    if (sender.hasPermission("mmissile.op")){
                        sender.sendMessage("===== 運営コマンド =====");
                        sender.sendMessage(prefix + "§7/mmissile start [MCID] [ミサイル名] §r: 指定したプレイヤーでミサイルを発射します");
                        sender.sendMessage(prefix + "§7/mmissile on/off §r: システムをon/offします");
                        sender.sendMessage(prefix + "§7/mmissile reload §r: リロードします");
                    }
                    return true;
                }
                else if (args[0].equals("list")){
                    sender.sendMessage(prefix + "ミサイル一覧");
                    for (Missile missile : missiles) sender.sendMessage(missile.name);
                    return true;
                }
                break;

            case 2:
                if (args[0].equals("start")){
                    if (!system){
                        sender.sendMessage(prefix+"現在OFFです");
                        return true;
                    }
                    Missile target = null;
                    for (Missile t : missiles) if (t.name.equals(args[1])) target = t;
                    if (target == null){
                        sender.sendMessage(prefix + "そのミサイルは存在しません");
                        return true;
                    }
                    if (target.start_command != null){
                        for (String c : target.start_command){
                            String com = c.replace("<player>", sender.getName());
                            ((Player) sender).performCommand(com);
                        }
                    }
                    if (target.start_c_command != null){
                        for (String c : target.start_c_command){
                            String com = c.replace("<player>", sender.getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), com);
                        }
                    }
                    PotionEffect p = ((Player) sender).getPotionEffect(PotionEffectType.INVISIBILITY);
                    int d = 0;
                    if (p != null) d = p.getDuration();
                    ItemStack head = ((Player) sender).getInventory().getHelmet();
                    if (target.head != null) {
                        ((Player) sender).getInventory().setHelmet(target.head.clone());
                        ((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, target.time.intValue() * 20, 1));
                    }
                    players.put(((Player) sender).getUniqueId(), new MissilePlayer(target, ((Player) sender).getLocation(), head, d));
                    Location l = ((Player) sender).getLocation();
                    l.setY(l.getY() + 2);
                    l.setPitch(-90F);
                    ((Player) sender).teleport(l);
                    ((Player) sender).setAllowFlight(true);
                    new Runnable(((Player) sender).getUniqueId()).runTaskTimer(mmissile,0 , period);
                    return true;
                }
                break;

            case 3:
                if (args[0].equals("start") && sender.hasPermission("mmissile.op")){
                    if (!system){
                        sender.sendMessage(prefix+"現在OFFです");
                        return true;
                    }
                    Missile target = null;
                    for (Missile t : missiles) if (t.name.equals(args[2])) target = t;
                    if (target == null){
                        sender.sendMessage(prefix + "そのミサイルは存在しません");
                        return true;
                    }
                    Player player = Bukkit.getPlayerExact(args[1]);
                    if (player == null){
                        sender.sendMessage(prefix + "そのプレイヤーは存在しません");
                        return true;
                    }
                    if (target.start_command != null){
                        for (String c : target.start_command){
                            String com = c.replace("<player>", player.getName());
                            player.performCommand(com);
                        }
                    }
                    if (target.start_c_command != null){
                        for (String c : target.start_c_command){
                            String com = c.replace("<player>", player.getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), com);
                        }
                    }
                    PotionEffect p = player.getPotionEffect(PotionEffectType.INVISIBILITY);
                    int d = 0;
                    if (p != null) d = p.getDuration();
                    ItemStack head = player.getInventory().getHelmet();
                    if (target.head != null){
                        player.getInventory().setHelmet(target.head.clone());
                        ((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, target.time.intValue() * 20, 1));
                    }
                    players.put(player.getUniqueId(), new MissilePlayer(target, player.getLocation(), head, d));
                    Location l = player.getLocation();
                    l.setY(l.getY() + 2);
                    l.setPitch(-90F);
                    player.teleport(l);
                    ((Player) sender).setAllowFlight(true);
                    new Runnable(player.getUniqueId()).runTaskTimer(mmissile,0 , period);
                    return true;
                }
                break;

            default:
                break;
        }
        sender.sendMessage(prefix+"/mmissile help でhelpを表示");
        return true;
    }
}
