package alexpet.recycler.listeners;

import alexpet.recycler.Recycler;
import alexpet.recycler.misc.HelperFunctions;
import alexpet.recycler.misc.HyperApi;
import alexpet.recycler.misc.RecyclerBackEnd;
import alexpet.recycler.misc.WeightedNode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class InventoryListeners implements Listener {
    Recycler plugin;
    HyperApi HyperAPI;

    HashMap<String, Integer> stableStorage = new HashMap<>();

    // ArrayList<HashMap<String,Integer>> storageList = new ArrayList<>();

    //RecyclerBackEnd backend = new RecyclerBackEnd();
    RecyclerBackEnd backend;


    public InventoryListeners(Recycler plugin, HyperApi HyperApi,RecyclerBackEnd backend) {
        this.plugin = plugin;
        this.HyperAPI = HyperApi;
        this.backend = backend;
    };

    @EventHandler
    public void createRecyclerBlock(BlockDispenseEvent event) {
        // creates and gives the

        // System.out.println("BLOCKDISPENSEEVENT");
        // System.out.println(event.getBlock());

        ItemStack dispensedBlock = event.getItem();
        if (dispensedBlock.getType() == Material.BEDROCK && event.getBlock().getType() == Material.DISPENSER) {
            // System.out.println("INSIDE IF");
            // event.setCancelled(true);
            backend.addStorage();

            ItemStack recyclerBlock = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
            ItemMeta recyclerMeta = recyclerBlock.getItemMeta();
            int len = backend.getlengthOfStorage();
            recyclerMeta.setDisplayName("Recycler_" + Integer.toString(len));// make sure to do len-1 when getting the
                                                                             // position in the arraylist later
            recyclerBlock.setItemMeta(recyclerMeta);
            event.setItem(recyclerBlock);

            Dispenser dispenser = (Dispenser) event.getBlock().getState();
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                public void run() {

                    dispenser.getSnapshotInventory().remove(event.getItem().getType());
                    dispenser.update();

                }
            }, 1L);

            // ItemStack recyclerDesignater = new ItemStack(Material.BEDROCK);
        }
    }

    @EventHandler
    public void moveEvent(InventoryMoveItemEvent event) {
        // System.out.println("RECYCLE EVENT TRIGGERED");
        Inventory invDest = event.getDestination();// a memory address
        System.out.println(invDest.toString());
        if (invDest.getType() != InventoryType.DISPENSER) {
            return;
        }
        Location destLoc = event.getDestination().getLocation();
        System.out.println(destLoc);

        List<Entity> nearbyEntities = HelperFunctions.getNearbyEntities(destLoc, 2);
        ItemFrame recycleFrame = null;

        for (Entity e : nearbyEntities) {
            System.out.println(e.getType());
            // Boolean check= HelperFunctions.checkBlock(b,Material.ITEM_FRAME);
            Boolean check = HelperFunctions.checkEntity(e, EntityType.ITEM_FRAME);
            if (check) {

                // Location item_frame_loc = new Location(Bukkit.getWorld("world"), e.getX(),
                // e.getY(), e.getZ());
                // contrapFrame = (ItemFrame) item_frame_loc.getBlock().getState();
                recycleFrame = (ItemFrame) e;
                break;
            }
        }

        if (recycleFrame != null) {
            // itemframe is found on the contraption
            System.out.println("recycle frame passed");
            ItemStack insideItem = recycleFrame.getItem();

            System.out.println(insideItem.getType());
            if (insideItem.getType() == Material.GREEN_GLAZED_TERRACOTTA) {

            } else if (insideItem.getType() == Material.RED_GLAZED_TERRACOTTA) {
                // RecyclerBackEnd backend = new RecyclerBackEnd();
                // event.setCancelled(true);

                if (!insideItem.hasItemMeta()) {
                    return;
                }
                String recyclerName = insideItem.getItemMeta().getDisplayName();

                System.out.println("IF PASSED -- red glazed terracotta found");
                if (event.getItem().hasItemMeta()) {
                    if (event.getItem().getItemMeta().getDisplayName().equals("Contraption Block")) {

                        String contrapLore = event.getItem().getItemMeta().getLore().get(0);
                        String[] splitLore = contrapLore.split("[,\\s]+");
                        for (int i = 0; i < splitLore.length; i++) {
                            System.out.println("SPLIT LORE = " + splitLore[i]);
                            if (splitLore[i].charAt(0) == '['
                                    || splitLore[i].charAt(splitLore[i].length() - 1) == ']') {
                                String item = splitLore[i].replace("[", "");
                                item = item.replace("]", "");

                                ItemStack output = new ItemStack(Material.getMaterial(item.toUpperCase()), Math.abs(1));
                                // System.out.println("OUTPUT: "+output);
                                event.getDestination().addItem(output);

                            }
                            System.out.println("HERE");
                        }
                        ItemStack outputRedstone = new ItemStack(Material.REDSTONE);
                        // System.out.println("OUTPUT: "+output);
                        event.getDestination().addItem(outputRedstone);

                        ///////////////////////
                        // delete the given item
                        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                            public void run() {

                                event.getDestination().removeItem(event.getItem());

                            }
                        }, 1L);

                        ///////////////////////

                        return;

                    }
                }
                String item = "minecraft:" + event.getItem().getType().name();
                System.out.println("ITEM BEING SEARCHED: " + item);

                ArrayList<WeightedNode> recipeNodeList = HyperAPI.getTargets(item.toLowerCase());

                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

                    public void run() {

                        event.getDestination().removeItem(event.getItem());

                    }
                }, 1L);

                ///////////////////////

                WeightedNode recipeNode = recipeNodeList.get(0);// we are picking the recipeNode here

                int recipeWeight = recipeNode.getWeight();// the amount produced by the recipe node

                // maybe rework this part later so it happens once and the check is closer to
                // the end?
                if (recipeWeight != 1) {
                    // more than 1 item is needed to go backwards
                    // refer to the stick/plank example --> as in it takes 4 sticks to revert to 2
                    // plank
                    // --> recipeWeight here is 4
                    // if(stableStorage.containsKey())
                    // backend.shouldProduce(,);
                    // backend.shouldProduce();
                    int recyclerIndex = Integer.parseInt(recyclerName.split("_")[1]) - 1;// this should get the index
                                                                                         // that the recycler is stored
                                                                                         // at in the array of recyclers

                    ArrayList<WeightedNode> parts = HyperAPI.getTargets(recipeNode.getSource());// all the itemNodes
                                                                                                // pointing to the
                                                                                                // recycler node
                    HashMap<String, Integer> itemNodeWeightsMap = new HashMap<>();
                    ArrayList<Integer> itemNodeWeightsNumbers = new ArrayList<>();

                    for (WeightedNode part : parts) {
                        itemNodeWeightsMap.put(part.getSource(), part.getWeight());
                        itemNodeWeightsNumbers.add(part.getWeight());
                    }
                    // itemNodeWeights.put("FinalOutPutWeight",recipeWeight);//adding this so the
                    // gcd can also be found
                    // ArrayList<Integer> scaledWeights = new ArrayList<>();
                    itemNodeWeightsNumbers.add(recipeWeight);

                    // !!!!!have both a hashmap and an arraylist --> use the arraylist to find the
                    // gcd and then loop through all the hashmap and scale by the gcd!!!!
                    int gcd = HelperFunctions.GCDofList(itemNodeWeightsNumbers);// scaling factor//i may need to change
                                                                                // this

                    for (Map.Entry<String, Integer> node : itemNodeWeightsMap.entrySet()) {
                        // String key = node.getKey();
                        int scaledVal = node.getValue() / gcd;

                        itemNodeWeightsMap.replace(node.getKey(), scaledVal);
                    }
                    int scaledAmountOfInput = recipeWeight / gcd;

                    boolean shouldProduce = backend.shouldProduce(item.toLowerCase(), scaledAmountOfInput,
                            recyclerIndex);

                    if (shouldProduce) {
                        ArrayList<WeightedNode> recipeParts = HyperAPI.getTargets(recipeNode.getSource());
                        for (WeightedNode recipePart : recipeParts) {
                            String nameChange = recipePart.getSource().replace("minecraft:", "");
                            int scaledWeight = recipePart.getWeight() / gcd;
                            ItemStack output = new ItemStack(Material.getMaterial(nameChange.toUpperCase()),
                                    Math.abs(scaledWeight));
                            // System.out.println("OUTPUT: "+output);
                            event.getDestination().addItem(output);

                        }

                    }

                    return;
                } // ends the check that the recipeNode's weight is not 1

                ArrayList<WeightedNode> parts = HyperAPI.getTargets(recipeNode.getSource());

                // ArrayList<ItemStack> returnVal = new ArrayList<>();

                for (WeightedNode node : parts) {
                    System.out.println(node);
                    String mat = node.getSource().replace("minecraft:", "");
                    // Material.getMaterial()

                    int amount = node.getWeight();
                    System.out.println("MATERIAL BEING GOTTEN: " + Material.getMaterial(mat.toUpperCase()));

                    ItemStack output = new ItemStack(Material.getMaterial(mat.toUpperCase()), Math.abs(amount));
                    System.out.println("OUTPUT: " + output);
                    event.getDestination().addItem(output);
                    // returnVal.add(output);
                }

            }
        }

    }

}
