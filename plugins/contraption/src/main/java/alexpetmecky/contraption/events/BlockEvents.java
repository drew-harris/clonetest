package alexpetmecky.contraption.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockEvents implements Listener {
    //test comment
    //code
    //code

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        Material blockType = event.getBlock().getType();
        if(blockType == Material.PURPLE_GLAZED_TERRACOTTA){
            event.setCancelled(true);
        }
        if(blockType == Material.PURPLE_GLAZED_TERRACOTTA){
            Block contrap = event.getBlock();
            //ItemStack conStack = new ItemStack(contrap,1);
            //ItemMeta conMeta = contrap.getItemMeta();
            //contrap.getPersistentDataContainer();

        }

    }
}
