package alexpetmecky.contraption.listeners;

import alexpetmecky.contraption.Contraption;
import alexpetmecky.contraption.misc.HelperFunctions;
import alexpetmecky.contraption.misc.HyperApi;
import alexpetmecky.contraption.misc.Recipe;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.material.MaterialData;

import java.io.PrintStream;
import java.util.ArrayList;

public class DispenserListener implements Listener {
    Contraption plugin;
    public DispenserListener(Contraption plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void dispenserEvent(BlockDispenseEvent event) {
        MaterialData data = event.getBlock().getState().getData();
        System.out.println(data);



        //running some tests to make sure the graph is actually made




        BlockFace targetFace = ((Dispenser) event.getBlock().getState().getData()).getFacing();//the face of the dispenser
        System.out.println(targetFace);
        /*
        if(event.getBlock().getLocation().add(0, 1, 0).getBlock().getType() == Material.ITEM_FRAME){
            System.out.println(event.getBlock().getLocation().add(0, 1, 0).getBlock().getType());
            System.out.println("HERE");
        }else{
            System.out.println("ITEM FRAME NOT FOUND");
            System.out.println(event.getBlock().getLocation().add(0, 1, 0).getBlock().getType());
        }
*/
        boolean itemFrameFound = false;
        ItemFrame itemFrame = null;
        for (Entity entity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 2, 2, 2)) {
            if (entity instanceof ItemFrame && entity.getLocation().getBlock().getRelative(((ItemFrame) entity).getAttachedFace()).equals(event.getBlock())) {
                itemFrameFound = true;
                itemFrame = (ItemFrame) entity;
            }
        }

        boolean checkHyperGraph = false;
        if (itemFrameFound) {
            System.out.println("Item Frame Was Found");
            ItemStack item = itemFrame.getItem();
            if (item.getType() == Material.MAGENTA_GLAZED_TERRACOTTA) {
                checkHyperGraph = true;
            } else {
                System.out.println("Failed");
            }

        }


        if (checkHyperGraph) {
            System.out.println("Check Hyper graph set to true");
            //this is to get the chests on each side; the right will contain the input, the left will contain the output
            Chest left = null;//output
            Chest right = null;//input

            System.out.println("Target Face: " + targetFace);
            if (targetFace.toString().equals("NORTH")) {
                //these values are specific to each direction
                System.out.println("HERE 0");
                if (event.getBlock().getLocation().subtract(1, 0, 0).getBlock().getType() == Material.CHEST && event.getBlock().getLocation().add(1, 0, 0).getBlock().getType() == Material.CHEST) {
                    System.out.println("HERE 1");
                    Block tempRight = event.getBlock().getLocation().add(1, 0, 0).getBlock();
                    System.out.println("HERE 2");
                    right = (Chest) tempRight.getState();
                    System.out.println("HERE 3");
                    Block tempLeft = event.getBlock().getLocation().subtract(1, 0, 0).getBlock();
                    System.out.println("HERE 4");
                    left = (Chest) tempLeft.getState();
                    System.out.println("HERE 5");
                }


                ///Chest chestState = target.getState();
                //Inventory chestInv = targetState.getBlockInventory();

            } else if (targetFace.toString().equals("SOUTH")) {
                if (event.getBlock().getLocation().subtract(1, 0, 0).getBlock().getType() == Material.CHEST && event.getBlock().getLocation().add(1, 0, 0).getBlock().getType() == Material.CHEST) {
                    Block tempLeft = event.getBlock().getLocation().add(1, 0, 0).getBlock();
                    left = (Chest) tempLeft.getState();

                    Block tempRight = event.getBlock().getLocation().subtract(1, 0, 0).getBlock();
                    right = (Chest) tempRight.getState();
                }

            } else if (targetFace.toString().equals("EAST")) {
                if (event.getBlock().getLocation().subtract(0, 0, 1).getBlock().getType() == Material.CHEST && event.getBlock().getLocation().add(1, 0, 1).getBlock().getType() == Material.CHEST) {
                    Block tempLeft = event.getBlock().getLocation().add(0, 0, 1).getBlock();
                    left = (Chest) tempLeft.getState();

                    Block tempRight = event.getBlock().getLocation().subtract(0, 0, 1).getBlock();
                    right = (Chest) tempRight.getState();
                }

            } else if (targetFace.toString().equals("WEST")) {
                if (event.getBlock().getLocation().subtract(0, 0, 1).getBlock().getType() == Material.CHEST && event.getBlock().getLocation().add(0, 0, 1).getBlock().getType() == Material.CHEST) {
                    Block tempRight = event.getBlock().getLocation().add(0, 0, 1).getBlock();
                    right = (Chest) tempRight.getState();

                    Block tempLeft = event.getBlock().getLocation().subtract(0, 0, 1).getBlock();
                    left = (Chest) tempLeft.getState();
                }
            } else {
                //block is facing the wrong direction, cancel the whole thing
                System.out.println("BLOCK IS FACING THE WRONG DIRECTION");
            }

            //this is what the user wants to use as input and output
            Inventory expectedInputInv = right.getBlockInventory();//something like oak logs
            Inventory expectedOutputInv = left.getBlockInventory();//something like a wood pickaxe

            System.out.println(expectedInputInv.getContents().toString());
            System.out.println(expectedOutputInv.getContents());

            System.out.println(expectedInputInv.getSize());
            ArrayList<ItemStack> givenInput = new ArrayList<>();
            ArrayList<ItemStack> givenOutput = new ArrayList<>();

            //the size is the amount of slots in the chest
            //adds to the expected input
            for (int i = 0; i < expectedInputInv.getSize(); i++) {
                if (expectedInputInv.getItem(i) != null) {
                    //checks if the inventory slot is empty
                    //here we have each individual item
                    givenInput.add(expectedInputInv.getItem(i));
                }
            }

            //adds to the expected output
            for (int i = 0; i < expectedOutputInv.getSize(); i++) {
                if (expectedOutputInv.getItem(i) != null) {
                    //checks if the inventory slot is empty
                    //here we have each individual item
                    givenOutput.add(expectedOutputInv.getItem(i));
                }
            }

            //for(int i=0; i<givenInput.size();i++){
            //    System.out.println(givenInput.get(i).getType());
            //}
            String file = "/static/HyperGraphMini.csv";
            HyperApi api = new HyperApi(file);

            //input is being turned into output
            if (givenOutput.size() > 1 && givenInput.size() == 1) {
                //deconstructing

                //givenInput.get(0).getType();
                String itemToSearch = String.valueOf(givenInput.get(0).getType());
                ArrayList<Recipe> initOutput = api.findIngredientsNew(itemToSearch);

                HelperFunctions.searchBreakDown(itemToSearch, givenOutput, api);
                //String itemToSearch,ArrayList ItemStack,

                //ArrayList<> = initOutput.getItemsRequired();
                if (initOutput.size() == 1) {

                } else if (givenOutput.size() == 1 && givenInput.size() > 1) {
                    //crafting
                } else if (givenOutput.size() == 1 && givenInput.size() == 1) {
                    //either crafting or taking apart
                } else {
                    //not really sure how we got here, this should be invalid but keep is as a catchall
                }
                for (int i = 0; i < givenOutput.size(); i++) {
                    //each output needs to be broken down into what the user wants, This should probably only be one item
                    //can write tests or just leave it, extra materials should be returned
                    System.out.println("------------------");
                    System.out.println(String.valueOf(givenOutput.get(i)));
                    System.out.println(String.valueOf(givenOutput.get(i).getType()));
                    System.out.println("------------------");
                    api.findIngredientsNew(String.valueOf(givenOutput.get(i).getType()));


                }


            }
            //closes the if statement that allows the hypergraph to be checked


        }
        //closes the dispenserEvent
    }
}
