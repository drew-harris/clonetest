package alexpet.splitter.listeners;

import alexpet.splitter.Splitter;
import alexpet.splitter.misc.HelperFunctions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
//import org.bukkit.block.data.type.Sign;
//org.bukkit.material.Sign materialSign = new Sign();
//org.bukkit.material.Sign signState = (org.bukkit.material.Sign)(block.getState());

//final org.bukkit.material.Sign materialSign;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//this works, it splits in 3 directions if the chest is placed in front and the C directive is used
public class RedStoneListener implements Listener {
    Splitter plugin;
    public RedStoneListener(Splitter plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void redStoneEvent(BlockRedstoneEvent redstoneEvent){
        //Block block = redstoneEvent.getBlock();
        //System.out.println(block);
    }

    @EventHandler
    public void createSplitterBlock(BlockDispenseEvent event){
        List<Entity> nearbyEntities;
        if(event.getBlock().getType() == Material.DISPENSER){
            Location destLoc = event.getBlock().getLocation();
            nearbyEntities= HelperFunctions.getNearbyEntities(destLoc,2);
            //check an item frame on the hopper for splitter block
            ItemFrame dispenserFrame = null;
            for (Entity e:nearbyEntities) {
                System.out.println(e.getType());
                //Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
                boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
                if(check){
                    System.out.println("Check passes");
                    dispenserFrame = (ItemFrame) e;
                    ItemStack inside = dispenserFrame.getItem();
                    if(inside.getType() == Material.GREEN_GLAZED_TERRACOTTA){
                        if(event.getItem().getType() != Material.GREEN_GLAZED_TERRACOTTA ){

                            return;//do nothing
                        }
                        ItemStack splitter= new ItemStack(Material.GREEN_GLAZED_TERRACOTTA,1);
                        ItemMeta splitterMeta = splitter.getItemMeta();
                        splitterMeta.setDisplayName("Splitter");
                        splitter.setItemMeta(splitterMeta);

                        event.setItem(splitter);


                        //Material splitter =
                    }
                    break;
                }
            }



        }
    }

    @EventHandler
    public void hopperInteraction(InventoryMoveItemEvent event){
        Inventory invDest = event.getDestination();//a memory address
        System.out.println(invDest.getType());

        List<Entity> nearbyEntities;
        if(invDest.getType() == InventoryType.HOPPER){
            //may need another test that this is the splitter so i dont mess up any other plugins


            Location destLoc = event.getDestination().getLocation();
            nearbyEntities= HelperFunctions.getNearbyEntities(destLoc,2);
            //check an item frame on the hopper for splitter block
            ItemFrame splitterFrame = null;
            for (Entity e:nearbyEntities) {
                System.out.println(e.getType());
                //Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
                boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
                if(check){
                    System.out.println("Check passes-splitter");
                    //Location item_frame_loc = new Location(Bukkit.getWorld("world"), e.getX(), e.getY(), e.getZ());
                    //contrapFrame = (ItemFrame) item_frame_loc.getBlock().getState();
                    splitterFrame = (ItemFrame) e;
                    break;
                }
            }



            if(splitterFrame.getItem().getItemMeta().getDisplayName().equals("Splitter")){
                List<Block> nearbyBlocks= HelperFunctions.getNearbyBlocks(destLoc,1);
                boolean check = false;
                Sign mysign = null;
                for (Block b:nearbyBlocks){
                    if(b.getType() == Material.OAK_WALL_SIGN){

                        check = true;
                        mysign = (Sign) b.getState();




                        break;
                    }

                }

                if(check){
                    // it is a splitter with a sign on it
                    //System.out.println(mysign.getAsString());
                    System.out.println(mysign.getLine(1));

                    String pattern = mysign.getLine(1);
                    String pointer = mysign.getLine(2);

                    System.out.println("Pattern length: "+pattern.length());
                    System.out.println("Pointer length: "+pointer.length());

                    //////SETTING THE FACE OF THE SIGN
                    Location targetLoc = event.getDestination().getLocation();
                    Location signLoc = mysign.getLocation();
                    //mysign.getBlock().getBlockData().getFacing()

                    //Location frontBase = signLoc.subtract(0,1,0);
                    Location frontBase = new Location(mysign.getWorld(), signLoc.getX(),signLoc.getY()-1,signLoc.getZ());
                    //Location frontBase = new
                    String face = mysign.getLine(3);

                    if(face.length() == 0){
                        //this if-else chain will determine the face if the face is not already typed
                        if(frontBase.subtract(0,0,1).getBlock().getType() ==Material.HOPPER){
                            //facing south
                            face="south";
                        }else if(frontBase.add(0,0,1).getBlock().getType() ==Material.HOPPER){
                            //facing north
                            face="north";
                        }else if(frontBase.subtract(1,0,0).getBlock().getType() ==Material.HOPPER){
                            //facing east
                            face="east";
                        }else if(frontBase.add(1,0,0).getBlock().getType() ==Material.HOPPER){
                            //facing west
                            face="west";
                        }else{
                            //error no hopper behind or 'infront' of the sign
                            System.out.println("ERROR WITH THE WAY THE SPLITTER IS SET UP");
                            return;
                        }
                        //////
                        mysign.setLine(3,face);
                        mysign.update();
                    }





                    //a note about sign lengths, it only holds the length of the string, including spaces
                    String[] splitPattern = new String[10];
                    String[] splitPointer = new String[10];

                    if(pattern.length() != 10){
                        return;
                    }

                    if(pointer.length() == 0){
                        //a pointer has not been added, put it at index 0
                        //splitPattern.fill(" ");
                        Arrays.fill(splitPointer," ");
                        splitPointer[0] = "^";

                    }else{
                        //splitPattern = pattern.split("|");
                        splitPointer = pointer.split("|");
                    }

                    int index =0;

                    for(String s:splitPointer){
                        if(s.equals("^")){
                            System.out.println("^ at position: "+index);
                            break;
                        }
                        index ++;
                    }

                    String currLetter = String.valueOf(pattern.charAt(index));
                    System.out.println("currLetter: "+currLetter);

                    String pathDirection;
                    if(currLetter.equalsIgnoreCase("a")){
                        pathDirection = "right";
                        splitPointer = HelperFunctions.increasePointer(splitPointer,index);
                        index++;

                    }else if(currLetter.equalsIgnoreCase("b")){
                        pathDirection="left";
                        splitPointer = HelperFunctions.increasePointer(splitPointer,index);
                        index++;
                    }else if (currLetter.equalsIgnoreCase("c")){
                        System.out.println("C FOUND: CENTER BEING CALLED");
                        pathDirection="center";
                        splitPointer = HelperFunctions.increasePointer(splitPointer,index);
                        index++;
                    }else{
                        //error-> student messed up pattern
                        System.out.println("CONFIRM UPDATES ARE HAPPENING");
                        System.out.println("ERROR; pattern messed up");
                        return;
                    }

                    //updates the sign after moving the pointer
                    System.out.println("JOIN: "+String.join("",splitPointer));
                    System.out.println("Tostring: "+splitPointer.toString());
                    mysign.setLine(2,String.join("",splitPointer));
                    mysign.update();



                    List<Block> blockByTarget= HelperFunctions.getNearbyBlocks(targetLoc,1);
                    System.out.println("LENGTH OF blocksByTarget: "+blockByTarget.size());

                    //setting up variables to check chest sides
                    boolean foundLeft;
                    boolean foundRight;
                    boolean splitDestFound=false;
                    System.out.println("LOCATION OF SIGN: "+signLoc);
                    Location rightDest = null;
                    Location leftDest = null;
                    Location centerDest = null;
                    System.out.println("FACE = "+face);

                    Location rightSide;
                    Location leftSide;
                    Location center;

                    //i dont think i can add/subtract; i have to get individually and do it, i think i am changing the signlLoc variable
                    if(face.equalsIgnoreCase("north")){
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()+1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()+1);
                        center = new Location(signLoc.getWorld(), signLoc.getX(),signLoc.getY()-1,signLoc.getZ());
                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                            centerDest=center;
                        }
                    }else if(face.equalsIgnoreCase("south")){
                        System.out.println("FACE == SOUTH");
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()-1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()-1);
                        System.out.println("rightSide coords: "+rightSide);
                        System.out.println("leftSide coords: "+leftSide);

                        center = new Location(signLoc.getWorld(),signLoc.getX(),signLoc.getY()-1,signLoc.getZ());//x,y-1,x

                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            System.out.println("Blocks found");
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                            centerDest = center;
                        }
                    }else if(face.equalsIgnoreCase("east")){
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()-1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()+1);
                        center = new Location(signLoc.getWorld(),signLoc.getX(),signLoc.getY()-1,signLoc.getZ());

                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                            centerDest=center;
                        }
                    }else if(face.equalsIgnoreCase("west")){
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()+1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()-1);
                        center = new Location(signLoc.getWorld(), signLoc.getX(),signLoc.getY()-1,signLoc.getZ());
                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                            centerDest=center;
                        }
                    }else{
                        System.out.println("ERROR no face direction given");
                    }


                    //could do this, but if i know all these locations and where the sign is facing, i should be able to just check 2 locations for chests/hoppers

                    if(!splitDestFound){
                        System.out.println("Conditions not met; no output destinations found");
                    }

                    ItemStack passedItem = event.getItem();

                    //Inventory leftInv;
                    //Inventory rightInv;

                    Dispenser leftDis=null;
                    Chest leftChest=null;

                    Dispenser rightDis=null;
                    Chest rightChest=null;
                    //BlockState state=null;

                    Dispenser centerDis=null;
                    Chest centerChest=null;


                    System.out.println("HERE 1");
                    if(leftDest.getBlock().getType() == Material.DISPENSER){
                        leftDis = (Dispenser) leftDest.getBlock().getState();
                        //state=leftDest.getBlock().getState();
                        //leftInv = leftDis.getSnapshotInventory();
                    }else{
                        //chest
                        leftChest = (Chest) leftDest.getBlock().getState();
                        //state=leftDest.getBlock().getState();
                        //leftInv = leftChest.getSnapshotInventory();
                    }
                    System.out.println("HERE 2");
                    if(rightDest.getBlock().getType() == Material.DISPENSER){
                        rightDis = (Dispenser) rightDest.getBlock().getState();
                        //rightInv = rightDis.getSnapshotInventory();
                        //state=rightDest.getBlock().getState();
                    }else{
                        //chest
                        rightChest = (Chest) rightDest.getBlock().getState();
                        //rightInv = rightChest.getSnapshotInventory();
                        //state=rightDest.getBlock().getState();
                    }

                    if(centerDest.getBlock().getType()==Material.DISPENSER){
                        centerDis = (Dispenser) centerDest.getBlock().getState();
                    }else{
                        //its not checked if its a chest above so here do a second check to ensure its a chest
                        if(centerDest.getBlock().getType()==Material.CHEST){
                            centerChest = (Chest) centerDest.getBlock().getState();
                        }else{
                            //throw some error
                            return;
                        }
                    }

                    System.out.println("HERE 3");

                    System.out.println("PATHDIRECTION: "+pathDirection);
                    if(pathDirection.equals("left")){
                        if(leftDis != null){
                            leftDis.getSnapshotInventory().addItem(passedItem);
                            leftDis.update();
                        }else{
                            leftChest.getSnapshotInventory().addItem(passedItem);
                            leftChest.update();
                        }
                    }else if(pathDirection.equals("center")){
                        if(centerDis !=null){
                            centerDis.getSnapshotInventory().addItem(passedItem);
                            centerDis.update();
                        }else{
                            //using the chest instead of the dispenser
                            centerChest.getSnapshotInventory().addItem(passedItem);
                            centerChest.update();
                        }
                    }else{
                        //move the item to the right
                        if(rightDis != null){
                            rightDis.getSnapshotInventory().addItem(passedItem);
                            rightDis.update();
                        }else{
                            rightChest.getSnapshotInventory().addItem(passedItem);
                            rightChest.update();
                        }
                    }
                    System.out.println("HERE 4");
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                        public void run() {

                            event.getDestination().removeItem(event.getItem());

                        }
                    }, 1L);














                }
            }
        }
        System.out.println("");
    }
}












/*

package alexpet.splitter.listeners;

import alexpet.splitter.Splitter;
import alexpet.splitter.misc.HelperFunctions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
//import org.bukkit.block.data.type.Sign;
//org.bukkit.material.Sign materialSign = new Sign();
//org.bukkit.material.Sign signState = (org.bukkit.material.Sign)(block.getState());

//final org.bukkit.material.Sign materialSign;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedStoneListener implements Listener {
    Splitter plugin;
    public RedStoneListener(Splitter plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void redStoneEvent(BlockRedstoneEvent redstoneEvent){
        //Block block = redstoneEvent.getBlock();
        //System.out.println(block);
    }

    @EventHandler
    public void createSplitterBlock(BlockDispenseEvent event){
        List<Entity> nearbyEntities;
        if(event.getBlock().getType() == Material.DISPENSER){
            Location destLoc = event.getBlock().getLocation();
            nearbyEntities= HelperFunctions.getNearbyEntities(destLoc,2);
            //check an item frame on the hopper for splitter block
            ItemFrame dispenserFrame = null;
            for (Entity e:nearbyEntities) {
                System.out.println(e.getType());
                //Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
                boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
                if(check){
                    System.out.println("Check passes");
                    dispenserFrame = (ItemFrame) e;
                    ItemStack inside = dispenserFrame.getItem();
                    if(inside.getType() == Material.GREEN_GLAZED_TERRACOTTA){
                        if(event.getItem().getType() != Material.GREEN_GLAZED_TERRACOTTA ){

                            return;//do nothing
                        }
                        ItemStack splitter= new ItemStack(Material.GREEN_GLAZED_TERRACOTTA,1);
                        ItemMeta splitterMeta = splitter.getItemMeta();
                        splitterMeta.setDisplayName("Splitter");
                        splitter.setItemMeta(splitterMeta);

                        event.setItem(splitter);


                        //Material splitter =
                    }
                    break;
                }
            }



        }
    }

    @EventHandler
    public void hopperInteraction(InventoryMoveItemEvent event){
        Inventory invDest = event.getDestination();//a memory address
        System.out.println(invDest.getType());

        List<Entity> nearbyEntities;
        if(invDest.getType() == InventoryType.HOPPER){
            //may need another test that this is the splitter so i dont mess up any other plugins


            Location destLoc = event.getDestination().getLocation();
            nearbyEntities= HelperFunctions.getNearbyEntities(destLoc,2);
            //check an item frame on the hopper for splitter block
            ItemFrame splitterFrame = null;
            for (Entity e:nearbyEntities) {
                System.out.println(e.getType());
                //Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
                boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
                if(check){
                    System.out.println("Check passes-splitter");
                    //Location item_frame_loc = new Location(Bukkit.getWorld("world"), e.getX(), e.getY(), e.getZ());
                    //contrapFrame = (ItemFrame) item_frame_loc.getBlock().getState();
                    splitterFrame = (ItemFrame) e;
                    break;
                }
            }



            if(splitterFrame.getItem().getItemMeta().getDisplayName().equals("Splitter")){
                List<Block> nearbyBlocks= HelperFunctions.getNearbyBlocks(destLoc,1);
                boolean check = false;
                Sign mysign = null;
                for (Block b:nearbyBlocks){
                    if(b.getType() == Material.OAK_WALL_SIGN){

                        check = true;
                        mysign = (Sign) b.getState();




                        break;
                    }

                }

                if(check){
                    // it is a splitter with a sign on it
                    //System.out.println(mysign.getAsString());
                    System.out.println(mysign.getLine(1));

                    String pattern = mysign.getLine(1);
                    String pointer = mysign.getLine(2);

                    System.out.println("Pattern length: "+pattern.length());
                    System.out.println("Pointer length: "+pointer.length());

                    //////SETTING THE FACE OF THE SIGN
                    Location targetLoc = event.getDestination().getLocation();
                    Location signLoc = mysign.getLocation();
                    //mysign.getBlock().getBlockData().getFacing()

                    //Location frontBase = signLoc.subtract(0,1,0);
                    Location frontBase = new Location(mysign.getWorld(), signLoc.getX(),signLoc.getY()-1,signLoc.getZ());
                    //Location frontBase = new
                    String face = mysign.getLine(3);

                    if(face.length() == 0){
                        //this if-else chain will determine the face if the face is not already typed
                        if(frontBase.subtract(0,0,1).getBlock().getType() ==Material.HOPPER){
                            //facing south
                            face="south";
                        }else if(frontBase.add(0,0,1).getBlock().getType() ==Material.HOPPER){
                            //facing north
                            face="north";
                        }else if(frontBase.subtract(1,0,0).getBlock().getType() ==Material.HOPPER){
                            //facing east
                            face="east";
                        }else if(frontBase.add(1,0,0).getBlock().getType() ==Material.HOPPER){
                            //facing west
                            face="west";
                        }else{
                            //error no hopper behind or 'infront' of the sign
                            System.out.println("ERROR WITH THE WAY THE SPLITTER IS SET UP");
                            return;
                        }
                        //////
                        mysign.setLine(3,face);
                        mysign.update();
                    }





                    //a note about sign lengths, it only holds the length of the string, including spaces
                    String[] splitPattern = new String[10];
                    String[] splitPointer = new String[10];

                    if(pattern.length() != 10){
                        return;
                    }

                    if(pointer.length() == 0){
                        //a pointer has not been added, put it at index 0
                        //splitPattern.fill(" ");
                        Arrays.fill(splitPointer," ");
                        splitPointer[0] = "^";

                    }else{
                        //splitPattern = pattern.split("|");
                        splitPointer = pointer.split("|");
                    }

                    int index =0;

                    for(String s:splitPointer){
                        if(s.equals("^")){
                            System.out.println("^ at position: "+index);
                            break;
                        }
                        index ++;
                    }

                    String currLetter = String.valueOf(pattern.charAt(index));
                    System.out.println("currLetter: "+currLetter);

                    String pathDirection;
                    if(currLetter.equalsIgnoreCase("a")){
                        pathDirection = "right";
                        splitPointer = HelperFunctions.increasePointer(splitPointer,index);
                        index++;

                    }else if(currLetter.equalsIgnoreCase("b")){
                        pathDirection="left";
                        splitPointer = HelperFunctions.increasePointer(splitPointer,index);
                        index++;
                    }else{
                        //error-> student messed up pattern
                        System.out.println("ERROR; pattern messed up");
                        return;
                    }

                    System.out.println("JOIN: "+String.join("",splitPointer));
                    System.out.println("Tostring: "+splitPointer.toString());
                    mysign.setLine(2,String.join("",splitPointer));
                    mysign.update();



                    List<Block> blockByTarget= HelperFunctions.getNearbyBlocks(targetLoc,1);
                    System.out.println("LENGTH OF blocksByTarget: "+blockByTarget.size());

                    boolean foundLeft;
                    boolean foundRight;
                    boolean splitDestFound=false;
                    System.out.println("LOCATION OF SIGN: "+signLoc);
                    Location rightDest = null;
                    Location leftDest = null;
                    System.out.println("FACE = "+face);

                    Location rightSide;
                    Location leftSide;

                    //i dont think i can add/subtract; i have to get individually and do it, i think i am changing the signlLoc variable
                    if(face.equalsIgnoreCase("north")){
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()+1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()+1);

                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                        }
                    }else if(face.equalsIgnoreCase("south")){
                        System.out.println("FACE == SOUTH");
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()-1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()-1);
                        System.out.println("rightSide coords: "+rightSide);
                        System.out.println("leftSide coords: "+leftSide);
                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            System.out.println("Blocks found");
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                        }
                    }else if(face.equalsIgnoreCase("east")){
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()-1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()-1,signLoc.getY()-1,signLoc.getZ()+1);
                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                        }
                    }else if(face.equalsIgnoreCase("west")){
                        rightSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()+1);
                        leftSide = new Location(signLoc.getWorld(), signLoc.getX()+1,signLoc.getY()-1,signLoc.getZ()-1);
                        if((rightSide.getBlock().getType() == Material.CHEST || rightSide.getBlock().getType() ==Material.DISPENSER) && (leftSide.getBlock().getType() == Material.CHEST || leftSide.getBlock().getType() ==Material.DISPENSER) ){
                            splitDestFound = true;
                            rightDest = rightSide;
                            leftDest = leftSide;
                        }
                    }else{
                        System.out.println("ERROR no face direction given");
                    }


                    //could do this, but if i know all these locations and where the sign is facing, i should be able to just check 2 locations for chests/hoppers

                    if(!splitDestFound){
                        System.out.println("Conditions not met; no output destinations found");
                    }

                    ItemStack passedItem = event.getItem();

                    //Inventory leftInv;
                    //Inventory rightInv;

                    Dispenser leftDis=null;
                    Chest leftChest=null;

                    Dispenser rightDis=null;
                    Chest rightChest=null;
                    //BlockState state=null;


                    System.out.println("HERE 1");
                    if(leftDest.getBlock().getType() == Material.DISPENSER){
                        leftDis = (Dispenser) leftDest.getBlock().getState();
                        //state=leftDest.getBlock().getState();
                        //leftInv = leftDis.getSnapshotInventory();
                    }else{
                        //chest
                        leftChest = (Chest) leftDest.getBlock().getState();
                        //state=leftDest.getBlock().getState();
                        //leftInv = leftChest.getSnapshotInventory();
                    }
                    System.out.println("HERE 2");
                    if(rightDest.getBlock().getType() == Material.DISPENSER){
                        rightDis = (Dispenser) rightDest.getBlock().getState();
                        //rightInv = rightDis.getSnapshotInventory();
                        //state=rightDest.getBlock().getState();
                    }else{
                        //chest
                        rightChest = (Chest) rightDest.getBlock().getState();
                        //rightInv = rightChest.getSnapshotInventory();
                        //state=rightDest.getBlock().getState();
                    }
                    System.out.println("HERE 3");

                    System.out.println("PATHDIRECTION: "+pathDirection);
                    if(pathDirection.equals("left")){
                        if(leftDis != null){
                            leftDis.getSnapshotInventory().addItem(passedItem);
                            leftDis.update();
                        }else{
                            leftChest.getSnapshotInventory().addItem(passedItem);
                            leftChest.update();
                        }
                    }else{
                        if(rightDis != null){
                            rightDis.getSnapshotInventory().addItem(passedItem);
                            rightDis.update();
                        }else{
                            rightChest.getSnapshotInventory().addItem(passedItem);
                            rightChest.update();
                        }
                    }
                    System.out.println("HERE 4");
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                        public void run() {

                            event.getDestination().removeItem(event.getItem());

                        }
                    }, 1L);














                }
            }
        }
        System.out.println("");
    }
}
*/