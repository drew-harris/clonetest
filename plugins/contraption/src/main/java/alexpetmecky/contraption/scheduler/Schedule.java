package alexpetmecky.contraption.scheduler;

import alexpetmecky.contraption.Contraption;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Schedule extends JavaPlugin {
    Contraption plugin;
    public Schedule(Contraption plugin){
        this.plugin=plugin;
    }

    BukkitTask sched = new BukkitRunnable() {
        @Override
        public void run() {

        }
    }.runTaskTimer(plugin, 20L, 20L);

}
