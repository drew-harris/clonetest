package alexpetmecky.contraption.listeners;

import alexpetmecky.contraption.Contraption;
import alexpetmecky.contraption.ContraptionRun;
import alexpetmecky.contraption.inventories.dropperInventory;
import alexpetmecky.contraption.misc.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Array;
import java.util.*;


public class InventoryListener implements Listener {
    Contraption plugin;
    boolean useTime;
    //ContraptionBackend backend = new ContraptionBackend();
    ContraptionBackend backend;



    public InventoryListener(Contraption plugin,boolean useTime,ContraptionBackend prefilledBackend){
        this.useTime=useTime;

        this.plugin=plugin;
        this.backend=prefilledBackend;
    }

    @EventHandler
    public void setContraptionBlocks(WorldLoadEvent event){
        System.out.println("------------------------------------------------------------------------");
        System.out.println("THIS IS WORKING, I SHOULD BE RUNNING WHEN THE WORLD IS INITIALIZING");
        System.out.println("------------------------------------------------------------------------");

    }

    public void playCreationSound(Player player){
        //player.playSound();
        //Player player = (Player) event.getPlayer();
        player.playSound(player.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_CHIME,0.5f,1.0f);
    }
    public void playFailureSound(Player player){
        player.playSound(player.getLocation(),Sound.BLOCK_DISPENSER_FAIL,0.5f,1.0f);
    }
    public void playSuccessParticle(Player player,Location location){
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GREEN,1.0F);
        //one is the default size
        player.spawnParticle(Particle.REDSTONE,location,50,dustOptions);
    }

    public void playFailureParticle(Player player, Location location){
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED,1.0F);
        player.spawnParticle(Particle.REDSTONE,location,50,dustOptions);
    }
    @EventHandler
    public void invEvent(InventoryOpenEvent event){
        System.out.println(event.getView().getTitle());
        if(event.getView().getTitle().equals("Dropper")){
            //creates the contraption block
            event.setCancelled(true);
            Player p = (Player) event.getPlayer();
            dropperInventory customInv = new dropperInventory();
            //Inventory customInv =
            p.openInventory(customInv.getInventory());
        }else if(event.getView().getTitle().equals("Dispenser")){
            //dont use this
            //use the eventhandler for item move events
            //ContraptionRun runner = new ContraptionRun();
            //runner.run();
        }

    }

    @EventHandler
    public void invMove(InventoryMoveItemEvent event){//triggered from hopper
        //uses the contraption block
        //add a check to make sure that it is the right items




        //System.out.println("HERE");
        //contraption execution called here
        Inventory invDest = event.getDestination();//a memory address
        System.out.println(invDest.getType());
        if(invDest.getType() != InventoryType.DISPENSER){
            return;
        }
        Location destLoc = event.getDestination().getLocation();
        System.out.println(destLoc);

        List<Entity> nearbyEntities= HelperFunctions.getNearbyEntities(destLoc,2);

        ItemFrame contrapFrame = null;
        for (Entity e:nearbyEntities) {
            System.out.println(e.getType());
            //Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
            Boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
            if(check){
                System.out.println("Check passes");
                contrapFrame = (ItemFrame) e;
                break;
            }
        }

        if(contrapFrame != null){
            //itemframe is found on the contraption
            System.out.println("Contraption Frame Not null");
            ItemStack insideItem = contrapFrame.getItem();
            System.out.println(insideItem.getItemMeta().getDisplayName());

            String insideItemName= insideItem.getItemMeta().getDisplayName();
            String insideItemPart = insideItemName.substring(0,17);
            String[] brokenName = insideItemName.split(" ");
            int contraptionNumber = Integer.parseInt(brokenName[2]);
            List<String> lore = insideItem.getItemMeta().getLore();
            System.out.println("The delay time is: "+lore.get(1));

            //////////
            if(insideItemPart.equals("Contraption Block")){
                //in the recycler make sure to change the contrapion block into the first part of it and to delete associated arraylist // it may be ok?
                System.out.println("IF PASSED");

                //i need the item that is being passes in the event
                Material passedItem = event.getItem().getType();
                //passedItem may need to become a string, but it is in the same format as what is stored in the backend
                //it does not include minecraft: --> I do not need to add it
                System.out.println("Passed Item: "+passedItem);

                System.out.println("Keys: ");
                backend.checkItem(contraptionNumber);

                if(backend.checkRecycler(contraptionNumber)){
                    System.out.println("IT IS A RECYCLER CONTRAPTION");
                    if(backend.shouldProducedRecycled(contraptionNumber,passedItem.toString())){
                        HashMap<String,Double> producedItems = backend.produceRecycled(contraptionNumber);
                        ///////////////////////
                        //delete the given item
                        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                            public void run() {

                                event.getDestination().removeItem(event.getItem());

                            }
                        }, 1L);

                        ///////////////////////
                        System.out.println("About to show produced items");
                        System.out.println("DELAY = "+Long.parseLong(lore.get(1))*20);

                        if(useTime){
                            //use the linear time

                            Bukkit.getScheduler().runTaskLater(plugin,new Runnable(){
                                @Override
                                public void run(){

                                    for(Map.Entry<String,Double> produced: producedItems.entrySet()){
                                        String name  = produced.getKey();
                                        double amount  = produced.getValue();
                                        System.out.println("Name: "+name+" Amount: "+amount);
                                        Material producedMaterial = Material.getMaterial(name);
                                        //ItemStack newItem = new ItemStack();
                                        ItemStack createdItem = new ItemStack(producedMaterial, (int) amount);
                                        event.getDestination().addItem(createdItem);
                                    }

                                }
                            }, Long.parseLong(lore.get(1))*20);


                        }else{
                            //do not use linear time

                            for(Map.Entry<String,Double> produced: producedItems.entrySet()){
                                String name  = produced.getKey();
                                double amount  = produced.getValue();
                                System.out.println("Name: "+name+" Amount: "+amount);
                                Material producedMaterial = Material.getMaterial(name);
                                //ItemStack newItem = new ItemStack();
                                ItemStack createdItem = new ItemStack(producedMaterial, (int) amount);
                                event.getDestination().addItem(createdItem);
                            }
                        }







                    }
                    return;
                }

                System.out.println("NOT A RECYCLER CONTRAPTION");

                if(backend.itemExistsInStorage(contraptionNumber,passedItem.toString())){
                    //it is in the storage, update it in currStorge and check if it can produce
                    //event.setCancelled(true);
                    backend.increaseItemInStorage(contraptionNumber,passedItem.toString(),1);


                    //the item needs to be deleted after being passed into the function
                    ///////////////////////
                    //delete the given item
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                        public void run() {

                            event.getDestination().removeItem(event.getItem());

                        }
                    }, 1L);

                    ///////////////////////



                    if(backend.shouldProduce(contraptionNumber)){
                        System.out.println("PRODUCING");
                        //production should happen
                        System.out.println("backend.produce Running");
                        HashMap<String,Double> produced =backend.produce(contraptionNumber);
                        //this only works for crafting i think

                        System.out.println("LENGHT OF PRODUCED: "+produced.size());
                        //as of APRIL 9 length of produced = 0
                        //that is the current problem, it should contain what is produced and its values
                        System.out.println("Outputting produced");
                        for(String key:produced.keySet()){
                            System.out.println("key: "+key+" amount: "+produced.get(key));
                        }
                        System.out.println("End outputting produced");
                        if(useTime){
                            Bukkit.getScheduler().runTaskLater(plugin,new Runnable(){
                                @Override
                                public void run(){
                                    for(Map.Entry<String,Double> itemSet:produced.entrySet()){
                                        System.out.println("Produce: "+itemSet.getKey() + " "+itemSet.getValue());

                                        Material producedMaterial = Material.getMaterial(itemSet.getKey());
                                        ItemStack createdItem = new ItemStack(producedMaterial);
                                        event.getDestination().addItem(createdItem);
                                        //spawn success particales
                                    }


                                }
                            }, Long.parseLong(lore.get(1))*20);


                        }else{
                            for(Map.Entry<String,Double> itemSet:produced.entrySet()){
                                System.out.println("Produce: "+itemSet.getKey() + " "+itemSet.getValue());

                                Material producedMaterial = Material.getMaterial(itemSet.getKey());
                                ItemStack createdItem = new ItemStack(producedMaterial);
                                event.getDestination().addItem(createdItem);
                                //spawn success particales
                            }
                        }


                    }else{
                        System.out.println("NOT ENOUGH TO PRODUCE");
                        //production should not happen
                        //not enough items in storage

                        backend.showInventory(contraptionNumber);

                    }


                }else{
                    //not in the storage, do something
                    //cause a backup or something?
                    //maybe make it an infinate loop that puts it back in the hopper
                    System.out.println("This item is not in the storage");
                }

            }
        }

    }
    @EventHandler
    public void invClose(InventoryCloseEvent event){

        //creates the contraption block

        if(event.getView().getTitle().equals("Contraption Creator")){
            HyperApi myApi = new HyperApi("STRING");

            ArrayList<ItemStack> input = new ArrayList<>();
            ArrayList<ItemStack> output = new ArrayList<>();
            ArrayList<ItemStack> middle = new ArrayList<>();

            ArrayList<String> stringInput = new ArrayList<>();
            ArrayList<String> stringOutput = new ArrayList<>();
            ArrayList<String> stringMiddle = new ArrayList<>();
            //System.out.println("3");

            int invSlot=0;
            for(ItemStack item:event.getInventory().getContents()){



                //slots (0-3,9-12,18-21) are input
                //slots(5-8,14-17,23-26) are output
                //slots(4,13,22) are redstone
                if(item != null){
                    if( (invSlot >=0 && invSlot <=3)||(invSlot >=9 && invSlot <=12) || (invSlot >=18 && invSlot <=21) ){
                        //input
                        input.add(item);
                        stringInput.add(item.getType().name());

                    } else if ((invSlot >=5 && invSlot <=8)||(invSlot >=14 && invSlot <=17) || (invSlot >=23 && invSlot <=26)) {
                        //output
                        output.add(item);
                        stringOutput.add(item.getType().name());

                    }else if( invSlot == 4 || invSlot == 13 || invSlot == 22){
                        //needs to have at least 1? redstone
                        middle.add(item);
                        stringMiddle.add(item.getType().name());

                        if(invSlot == 13){
                            if(!item.getType().name().equals("REDSTONE")){
                                System.out.println("REDSTONE NOT GIVEN-WILL NOT MAKE CONTRAPTION");
                                return;
                            }
                        }
                    }else{
                        System.out.println("ERROR--> SLOT: "+invSlot+" NOT CAUGHT IN IF-ELSE CHAIN");
                    }
                }



                invSlot +=1;

            }
            System.out.println("4");
        //do math here
            //String file = "/Users/alexpetmecky/Desktop/pluginStuff/ContraptionFull/src/main/resources/HyperGraphMini.csv";
            //HyperApi myAPI = new HyperApi(file);

            //to make this more efficint have them share as many variables as possible
            LinkedList<NodeMeta> path = null;
            ItemStack contraptionBlock = new ItemStack(Material.BLUE_GLAZED_TERRACOTTA);
            ItemMeta contrapMeta = contraptionBlock.getItemMeta();



            //naming the contraption
            //////////
            //if the code fails to find a path and create a contraption, make sure to delete the contraption from the storage
            int contrapNum = backend.getLength();
            contrapMeta.setDisplayName("Contraption Block "+Integer.toString(contrapNum));
            //add this contraption block to the list


            //////////




            if(stringInput.size() == 1 && stringOutput.size() >1){
                //This is now recycling
                System.out.println("Recycling");



                backend.addStorage();//backend only gets added if a test is passed
                //this is currently just testing it may need to be reworked
                backend.setIsRecycler(contrapNum,true);
                backend.setRecyclerInput(contrapNum,stringInput.get(0));

                int pathLen=0;





                for(String item:stringOutput){
                    SearchReturn tempReturn = myApi.searchGraphSingle(stringInput.get(0), item);
                    path = HelperFunctions.generatePath(tempReturn);
                    //System.out.println("SIZE OF PATH: " + path.size());
                    if(path.size() > pathLen){
                        pathLen = path.size();
                    }
                    System.out.println("SIZE OF PATH: " + path.size());

                    HelperFunctions.printPath(path);//use this as a test function

                    double amountPerStartItem = HelperFunctions.findAmountPerPath(path);//i need to test this function still
                    //amountPerStartItem is the amount that can be produced for 1 unit of input

                    double amountProduced = 1/amountPerStartItem;

                    //System.out.println(item + " output: " + amountProduced);

                    backend.insertToProducedPerItem(contrapNum,item,amountProduced);
                    backend.insertToCurrStorages(contrapNum,item);




                }


                //setting lore
                String lore = stringInput + " To " + stringOutput;
                ArrayList<String> loreList = new ArrayList<String>();
                loreList.add(lore);
                loreList.add(String.valueOf(pathLen));
                contrapMeta.setLore(loreList);

                //putting block together // it gets named above, where contrapNum is initialized
                contraptionBlock.setItemMeta(contrapMeta);
                //giving the player the contraption block
                event.getPlayer().getInventory().addItem(contraptionBlock);

                Player player = (Player) event.getPlayer();
                Location loc = event.getInventory().getLocation();

                playCreationSound(player);
                playSuccessParticle(player,loc);

                System.out.println("Done");



            }else if(stringOutput.size() == 1 && stringInput.size() >1) {
                //this is now crafting
                //input here means input on the graph, output is what is being made
                System.out.println("Crafting");

                backend.addStorage();
                for(String produced: stringOutput){

                    //System.out.println("Adding to amountProducedPerSet"+produced);
                    backend.insertToAmountProducedPerSet(contrapNum,produced,1);
                }

                int pathLen = 0;
                for (String item : stringInput) {


                    SearchReturn tempReturn = myApi.searchGraphSingle(stringOutput.get(0), item);
                    //item here is the "final node" in the search (for breaking down pick, this could be ingot)

                    path = HelperFunctions.generatePath(tempReturn);
                    if(path.size() > pathLen){
                        pathLen = path.size();
                    }
                    System.out.println("SIZE OF PATH: " + path.size());

                    HelperFunctions.printPath(path);//use this as a test function


                    double amountPerStartItem = HelperFunctions.findAmountPerPath(path);//i need to test this function still
                    //amountPerStartItem is the amount that can be produced for 1 unit of input


                    //im not going to use all the item before stuff
                    System.out.println(item + " HERE!!!!output: " + amountPerStartItem);

                    backend.insertToProducedPerItem(contrapNum, item, amountPerStartItem);//putting in how much is created per item given

                    backend.insertToCurrStorages(contrapNum,item);
                    //String itemBeforeFinal = amountPerStartItemMinus2.keySet();
                    //Sound.BLOCK_AMETHYST_BLOCK_CHIME;


                }


                    //setting lore
                    String lore = stringInput + " To " + stringOutput;
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(lore);
                    loreList.add(String.valueOf(pathLen));
                    contrapMeta.setLore(loreList);

                    //putting block together // it gets named above, where contrapNum is initialized
                    contraptionBlock.setItemMeta(contrapMeta);
                    //giving the player the contraption block
                    event.getPlayer().getInventory().addItem(contraptionBlock);

                    Player player = (Player) event.getPlayer();
                    Location loc = event.getInventory().getLocation();

                    playCreationSound(player);
                    //playSuccessParticle(player,loc);

                    System.out.println("Done");





            }else if(stringInput.size() == 1 && stringOutput.size() ==1) {
                //unclear if crafting or uncrafting
                System.out.println("Crafting or Recycling");
                //backend.addStorage();

                //to determine if crafting or recycling run pathfinding twice and see which gets a result; if both fail its an invalid recipe
                String itemIn = stringInput.get(0);
                String itemOut = stringOutput.get(0);
                SearchReturn tempReturn = myApi.searchGraphSingle(itemOut, itemIn);
                System.out.println("tempReturn: "+tempReturn);
                int pathLen=0;
                if(tempReturn == null){
                    //crafting input to output
                    tempReturn = myApi.searchGraphSingle(itemIn,itemOut);
                    if(tempReturn == null){
                        System.out.println("INVALID RECIPE");
                        return;
                    }
                    path = HelperFunctions.generatePath(tempReturn);
                    if(path.size() > pathLen){
                        pathLen = path.size();
                    }

                    backend.addStorage();

                    backend.insertToAmountProducedPerSet(contrapNum,stringOutput.get(0),1);
                    double amountPerStartItem = HelperFunctions.findAmountPerPath(path);

                    System.out.println(stringInput.get(0) + " output: " + amountPerStartItem);
                    amountPerStartItem = 1/amountPerStartItem;
                    System.out.println("????HERE: amountperStartItem: "+amountPerStartItem);
                    backend.insertToProducedPerItem(contrapNum, stringInput.get(0), amountPerStartItem);//putting in how much is created per item given

                    backend.insertToCurrStorages(contrapNum,stringInput.get(0));

                    String lore = stringInput + " To " + stringOutput;
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(lore);
                    loreList.add(String.valueOf(pathLen));
                    contrapMeta.setLore(loreList);

                    //putting block together // it gets named above, where contrapNum is initialized
                    contraptionBlock.setItemMeta(contrapMeta);
                    //giving the player the contraption block
                    event.getPlayer().getInventory().addItem(contraptionBlock);

                    Player player = (Player) event.getPlayer();

                    System.out.println("1");
                    InventoryHolder holder = event.getInventory().getHolder();
                    System.out.println("HOLDER: "+holder);
                    System.out.println("2");
                    Location loc=null;
                    System.out.println("3");
                    System.out.println( holder.getClass());

                    System.out.println("Holder Inventory Location"+holder.getInventory().getLocation());
                    loc = ((Dropper) holder).getBlock().getLocation();

                    System.out.println("5");
                    System.out.println("Player: "+player);
                    System.out.println("6");
                    System.out.println("Event inventory location: "+loc);
                    System.out.println("7");
                    //player.playSound(event.getPlayer().getLocation(),Sound.BLOCK_AMETHYST_BLOCK_CHIME,0.5f,1.0f);
                    playCreationSound(player);
                    System.out.println("8");

                    playSuccessParticle(player,loc);
                    System.out.println("9");



                }else{//temp return is not null


                    if(tempReturn == null){
                        System.out.println("INVALID RECIPE");
                        return;
                    }
                    path = HelperFunctions.generatePath(tempReturn);
                    if(path.size() > pathLen){
                        pathLen = path.size();
                    }


                    backend.addStorage();
                    backend.setIsRecycler(contrapNum,true);
                    backend.setRecyclerInput(contrapNum,stringInput.get(0));
                    //path = HelperFunctions.generatePath(tempReturn);

                    //something is wrong with the math here

                    //HelperFunctions.printPath(path);//use this as a test function
                    double amountPerStartItem = HelperFunctions.findAmountPerPath(path);//i need to test this function still
                    //amountPerStartItem is the amount that can be produced for 1 unit of input
                    String item = stringOutput.get(0);


                    //tempReturn = myApi.searchGraphSingle(itemIn,itemOut);
                    pathLen=0;
                    /*
                    int pathLenHere=0;
                    if(tempReturn == null){
                        System.out.println("INVALID RECIPE");
                        return;
                    }
                    path = HelperFunctions.generatePath(tempReturn);
                    if(path.size() > pathLenHere){
                        pathLenHere = path.size();
                    }
                    */

                    System.out.println("amountPerStartItem:"+amountPerStartItem);

                    backend.insertToProducedPerItem(contrapNum,item,amountPerStartItem);
                    backend.insertToCurrStorages(contrapNum,item);

                    String lore = stringInput + " To " + stringOutput;
                    ArrayList<String> loreList = new ArrayList<String>();
                    loreList.add(lore);
                    //loreList.add(String.valueOf(pathLenHere));
                    loreList.add(String.valueOf(pathLen));

                    contrapMeta.setLore(loreList);

                    //putting block together // it gets named above, where contrapNum is initialized
                    contraptionBlock.setItemMeta(contrapMeta);
                    //giving the player the contraption block
                    event.getPlayer().getInventory().addItem(contraptionBlock);
                    System.out.println("Done");

                    Player player = (Player) event.getPlayer();
                    Location loc = event.getInventory().getLocation();
                    playCreationSound(player);
                    playSuccessParticle(player,loc);

                }
                //need to test, determine what failed output looks like to see if recycling or crafting


            }else{
                //contitions not satisfied, user messed up
                //backend.addStorage() not run here
                Player player = (Player) event.getPlayer();
                Location loc = event.getInventory().getLocation();
                playFailureSound(player);
                playFailureParticle(player,loc);
            }

            }

        }


    @EventHandler
    public void invInteract(InventoryClickEvent event){
        //String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
        if(event.getCurrentItem() != null){
            if(event.getCurrentItem().hasItemMeta()){
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")){
                    event.setCancelled(true);
                }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("Redstone Dust Here")){
                    //event.getCurrentItem().
                    event.getClickedInventory().removeItem(event.getCurrentItem());
                }
            }
        }





    }


}

