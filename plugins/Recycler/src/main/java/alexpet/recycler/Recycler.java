package alexpet.recycler;

import alexpet.recycler.commands.GetRecyclerPersistance;
import alexpet.recycler.listeners.InventoryListeners;
import alexpet.recycler.misc.HyperApi;
import alexpet.recycler.misc.RecyclerBackEnd;
import org.bukkit.plugin.java.JavaPlugin;

public final class Recycler extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        HyperApi api = new HyperApi();
        RecyclerBackEnd backend = new RecyclerBackEnd();
        getServer().getPluginManager().registerEvents(new InventoryListeners(this, api, backend), this);

        getCommand("giverblocks").setExecutor(new GetRecyclerPersistance(backend));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
