package alexpet.splitter;

import alexpet.splitter.listeners.RedStoneListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Splitter extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new RedStoneListener(this),this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
