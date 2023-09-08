package alexpetmecky.contraption;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class ContraptionRun {
    public ContraptionRun(){

    }


    public void run(){
        int radius = 100;
        System.out.println("1");
        Location loc = new Location(getServer().getWorld("world"),0.0,0.0,0.0);
        System.out.println("2");
        World world = loc.getWorld();
        System.out.println("3");
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = world.getBlockAt(loc.getBlockX()+x, loc.getBlockY()+y, loc.getBlockZ()+z);

                    if (block.getState() instanceof ItemFrame) {
                        System.out.println("4");
                        //inside here is a given
                        //run code
                        ItemFrame myFrame = (ItemFrame) block.getBlockData();
                        ItemStack innerItem = myFrame.getItem();

                        ItemMeta innerItemMeta =  innerItem.getItemMeta();
                        PersistentDataContainer data = innerItemMeta.getPersistentDataContainer();
                        Set<NamespacedKey> keys = data.getKeys();

                        //System.out.println("Data: "+data.getKeys());
                        NamespacedKey key = keys.iterator().next();
                        String path = data.get(key,PersistentDataType.STRING);
                        System.out.println("PATH");
                        System.out.println(path);

                        BlockFace blockface= myFrame.getAttachedFace();
                        System.out.println("The blockface is: "+blockface);
                        //check that the itemframe has a dispenser on it
                        //get the path
                    }
                }
            }
        }
    }
}
