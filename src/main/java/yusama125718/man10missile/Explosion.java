package yusama125718.man10missile;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Explosion {

    public static class Settings{
        public String name;
        public Boolean armor;
        public Boolean team;
        public Double radius;
        public Double damage;

        public Settings(String Name, Boolean Armor, Boolean Team, Double Radius, Double Damage){
            name = Name;
            armor = Armor;
            team = Team;
            radius = Radius;
            damage = Damage;
        }
    }

    public static void Bomb(Player p, Settings set){
        Location ploc = p.getLocation();
        p.getWorld().playSound(ploc, Sound.ENTITY_GENERIC_EXPLODE, 2F, 1F);
        p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, ploc, 20);
        List<Player> team = new ArrayList<>();
        if (set.team){
            for (Team t : p.getScoreboard().getTeams()){
                for (String m : t.getEntries()){
                    Player x = Bukkit.getPlayer(m);
                    if (x != null) team.add(x);
                }
            }
        }
        for (Player t : p.getWorld().getPlayers()){
            if (t.getGameMode().equals(GameMode.CREATIVE) || t.getGameMode().equals(GameMode.SPECTATOR) || team.contains(t) || t.equals(p)) continue;
            Location tloc = t.getLocation();
            if (!(ploc.getX() - set.radius < tloc.getX() && ploc.getX() + set.radius > tloc.getX() && ploc.getY() - set.radius < tloc.getY() && ploc.getY() + set.radius > tloc.getY() && ploc.getZ() - set.radius < tloc.getZ() && ploc.getZ() + set.radius > tloc.getZ()))  return;
            if (set.armor) t.damage(set.damage);
            else t.setHealth(t.getHealth() - set.damage);
        }
    }
}
