package alexpetmecky.contraption.commands;

import alexpetmecky.contraption.misc.ContraptionBackend;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetPersistantBlocks implements CommandExecutor {
    ContraptionBackend backend;

    public GetPersistantBlocks(ContraptionBackend backend) {
        this.backend=backend;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("giveBlocks")){
                int length = backend.getLength();
                for(int i=0;i<length;i++){




                    ItemStack newContrap = new ItemStack(Material.BLUE_GLAZED_TERRACOTTA);
                    ItemMeta contrapMeta = newContrap.getItemMeta();
                    contrapMeta.setDisplayName("Contraption Block " +String.valueOf(i));
                    //List<String> lore = contrapMeta.getLore();
                    System.out.println("1");
                    System.out.println("2");


                    boolean isRecycler =  backend.checkRecycler(i);
                    ArrayList<Set<String>> in_out_keys= new ArrayList<>();
                    if(isRecycler){
                        in_out_keys = backend.getRecyclerInputOutputAtLocation(i);
                    }else{
                        in_out_keys =  backend.getInputOutputAtLocation(i);

                    }

                    ArrayList<String> input = new ArrayList<>();
                    input.addAll(in_out_keys.get(0));
                    ArrayList<String> output = new ArrayList<>();
                    output.addAll(in_out_keys.get(1));

                    String loreString = input + " To " + output;
                    System.out.println("LORESTRING: "+loreString);

                    ArrayList<String> lore = new ArrayList<String>();

                    lore.add(loreString);
                    lore.add("0");
                    System.out.println("3");
                    contrapMeta.setLore(lore);

                    System.out.println("4");
                    newContrap.setItemMeta(contrapMeta);

                    System.out.println("5");
                    player.getInventory().addItem(newContrap);
                    System.out.println("6");
                }
            }
        }

        return true;
    }
}
