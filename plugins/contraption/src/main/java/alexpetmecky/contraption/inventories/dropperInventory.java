package alexpetmecky.contraption.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class dropperInventory implements InventoryHolder {
    private Inventory inv;



    public dropperInventory(){
        inv = Bukkit.createInventory(this, InventoryType.CHEST,"Contraption Creator");
        init();
    }
    public void init(){
        //ItemStack = new ItemStack(Material.DIAMOND_SWORD);
        //ItemStack item;
        //item = createItem("CUSTOM ITEM",Material.ACACIA_BOAT, Collections.singletonList("THIS IS CUSTOM LORE"));

        //inv.setItem(0,item);

        ItemStack item;
        item = createItem(" ",Material.BLACK_STAINED_GLASS_PANE,Collections.singletonList("<--Input"));
        inv.setItem(4,item);

        item = createItem("Redstone Dust Here",Material.BLACK_STAINED_GLASS_PANE,Collections.singletonList(""));
        inv.setItem(13,item);

        item = createItem(" ",Material.BLACK_STAINED_GLASS_PANE,Collections.singletonList("Output -->"));
        inv.setItem(22,item);
    }



    private ItemStack createItem(String name,Material mat, List<String> lore){
        ItemStack item = new ItemStack(mat,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
