package alexpetmecky.contraption;

import alexpetmecky.contraption.commands.GetPersistantBlocks;
import alexpetmecky.contraption.events.BlockEvents;
import alexpetmecky.contraption.inventories.dropperInventory;
import alexpetmecky.contraption.listeners.DispenserListener;
import alexpetmecky.contraption.listeners.InventoryListener;
import alexpetmecky.contraption.misc.ContraptionBackend;
import alexpetmecky.contraption.onstart.PersistantContraptions;
import org.bukkit.plugin.java.JavaPlugin;

public final class Contraption extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        //dropperInventory customInv = new dropperInventory();
        //customInv.
        //getServer().getPluginManager().registerEvents(new DispenserListener(this),this);

        System.out.println("---Start---");

        //the persistantContraption will return a prefilled backed, then it will be passed to the inventory listener
        ContraptionBackend backend = new ContraptionBackend();

        PersistantContraptions persistance = new PersistantContraptions(backend);
        boolean getPersistence=true;
        if(getPersistence){
            backend = persistance.setContraptions();
        }

        getCommand("giveBlocks").setExecutor(new GetPersistantBlocks(backend));


        getServer().getPluginManager().registerEvents(new InventoryListener(this,false,backend),this);

        getServer().getPluginManager().registerEvents(new BlockEvents(),this);
        System.out.println("Contraption Starter");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
