package yusama125718.man10missile;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static yusama125718.man10missile.Man10Missile.players;

public class RemovePlayer extends BukkitRunnable {

    private final UUID p;

    public RemovePlayer(UUID player){p = player;}

    @Override
    public void run(){
        players.remove(p);
        System.out.println("iii");
    }
}
