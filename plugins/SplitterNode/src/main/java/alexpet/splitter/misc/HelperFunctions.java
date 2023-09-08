package alexpet.splitter.misc;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class HelperFunctions {
    public static String[] increasePointer(String[] pointerArray,int currIndex){
        //wraps the pointer around
        if(currIndex ==9){
            pointerArray[9] =" ";
            pointerArray[0] = "^";
            return pointerArray;
        }

        pointerArray[currIndex] = " ";
        currIndex +=1;
        pointerArray[currIndex] = "^";
        return pointerArray;
    }
    public static List<Entity> getNearbyEntities(Location location, double radius){
        //List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 15, 15, 15);
        //List<Entity> entities
        //return entities;
        //location.getWorld().getNearbyEntities()
        List<Entity> entities =(List<Entity>)location.getWorld().getNearbyEntities(location, radius, radius, radius);
        //Collections<Entity> entities =location.getWorld().getNearbyEntities(location, radius, radius, radius, (entity) -> entity.getType() == EntityType.ITEM_FRAME);
        return entities;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static boolean checkEntity(Entity test, EntityType target){
        if(test.getType() == target){
            return true;
        }else{
            return false;
        }
    }
}
