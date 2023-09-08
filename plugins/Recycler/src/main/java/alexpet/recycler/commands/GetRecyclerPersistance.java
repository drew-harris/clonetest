package alexpet.recycler.commands;

import alexpet.recycler.misc.RecyclerBackEnd;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GetRecyclerPersistance implements CommandExecutor {

    RecyclerBackEnd backend;
    public GetRecyclerPersistance(RecyclerBackEnd backend){
        this.backend = backend;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("giveRBlocks")){
                if(args.length > 0){
                    System.out.println("First Argument: "+args[0]);
                    int recyclersToProduce = Integer.parseInt(args[0]);

                    for(int i=0;i<recyclersToProduce;i++){
                        backend.addStorage();
                        int recyclerNumber = backend.getlengthOfStorage();
                        String name = "Recycler_"+Integer.toString(recyclerNumber);

                        ItemStack recyclerBlock = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
                        ItemMeta recyclerMeta = recyclerBlock.getItemMeta();
                        recyclerMeta.setDisplayName(name);

                        recyclerBlock.setItemMeta(recyclerMeta);
                        player.getInventory().addItem(recyclerBlock);


                    }

                }else{
                    System.out.println("No argument given");
                }

            }
        }

        return true;
    }

}
