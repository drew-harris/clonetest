package org.huminlabs.huminlabplugin;

import com.humingamelab.mc.Clientoperation.RegisterPlayerMutation;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.huminlabs.huminlabplugin.NPC.NPC;
import org.huminlabs.huminlabplugin.Objective.ObjectiveStorage;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/*
 *   EventListeners class handles all events that occur in the game. This includes player movement, player interaction, and inventory events.
 *  It also handles the NPC head follow and generally handles all events that could trigger a change in the stage machine.
 */
public class EventListeners implements Listener {
    //TODO
    //Line 99, quickFrameEval, make sure to add itemframes to the arraylist during world editing
    private HuMInLabPlugin plugin;
    static ArrayList<InventoryHolder> chests = new ArrayList<InventoryHolder>();
    private static ArrayList<Chest> lesson4Chests = new ArrayList<>();
    ArrayList<GlowItemFrame> u2l1ItemFrames = new ArrayList<GlowItemFrame>();
    private static Chest u2Lesson3SantiChest;

    private static Inventory[] u1l3orders = new Inventory[3]; // [wool, stone, wood]
    private static Inventory[] u1l3sorters = new Inventory[4]; // [wool, stone, wood, other]

    private static Inventory[] u1l4orders = new Inventory[3]; // [smelter, tool, book]
    private static Inventory[] u1l4incoming = new Inventory[5]; // [smelter, tool, book]

    private static Inventory[] u1l5Outgoing = new Inventory[1];

    private static Inventory u2l2Pedastal;

    public EventListeners(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method is called when a player joins the game.
     * It performs various actions when a player joins, such as adding the player to the objective storage,
     * updating the player's backend information, and setting the player's stage and objective.
     *
     * @param event The PlayerJoinEvent triggered when a player joins the game.
     */

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println("Player joined!");
        Player player = event.getPlayer();
        PlayerPointer playerPointer = ObjectiveStorage.getPlayerPointer(player);
        ObjectiveStorage.addPlayerPointer(playerPointer);

        RegisterPlayerMutation m = RegisterPlayerMutation.builder().name(player.getName()).build();
        try {
            RegisterPlayerMutation.Data data = plugin.api.executeMutation(m).get();
            System.out.println("Registered player: " + data.registerPlayer.id);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.toString());
            System.out.println("Failed to register player");
        }
        //WorldLoader.findWorld(player);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "recipe give " + player.getName() + " *");

        try {
            RegisterPlayerMutation.Data response = this.plugin.api.executeMutation(new RegisterPlayerMutation(player.getName())).get();
            System.out.println("Registered player: " + response.registerPlayer.name);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Couldn't register player!!");

        }

        HuMInLabPlugin.npcManager.loadNPCs(player);
        HuMInLabPlugin.backendRequestHandler.userBackendUpdate(player.getUniqueId().toString(), player.getName());

        Commands.setStage(player, playerPointer, playerPointer.getUnit(), playerPointer.getObjectiveID());
        ObjectiveStorage.updateObjective(player);
    }

    /**
     * This method is called when a player moves.
     * It handles the head movement of NPCs and performs lesson events for the player.
     *
     * @param event The PlayerMoveEvent triggered when a player moves.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer();
        ServerGamePacketListenerImpl playerConnection = craftPlayer.getHandle().connection;

        //NPC Head Follow
        for (NPC npc : plugin.npcManager.getNPCs()) {
            Location npcLocation = npc.npc.getBukkitEntity().getLocation();
            npcLocation.setDirection(event.getPlayer().getLocation().subtract(npcLocation).toVector());
            float yaw = npcLocation.getYaw();
            float pitch = npcLocation.getPitch();

            //Rotate head - horizontal head movement
            //Move Entity - vertical head movement

            playerConnection.send(new ClientboundRotateHeadPacket(npc.npc, (byte) ((yaw % 360) * 256 / 360)));
            playerConnection.send(new ClientboundMoveEntityPacket.Rot(npc.npc.getBukkitEntity().getEntityId(), (byte) ((yaw % 360) * 256 / 360), (byte) ((pitch % 360) * 256 / 360), npc.npc.isOnGround()));
        }

        //Lesson Events
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        String objectiveID = playerPointer.getObjectiveID();
        String unit = playerPointer.getUnit();

        Location playerLocation = player.getLocation();

        switch (unit) {
            case "1":
                switch (objectiveID) {
                    case "1.13":
                        if (inventory.contains(Material.STONE_AXE)) {
                            Commands.setStage(player, playerPointer, "1", "1.14");
                        }
                        break;

                    case "T.2":
                        //Just move
                        Commands.setStage(player, playerPointer, "1", "T.3");
                        plugin.dialogueManager.runDialogue("T1", player, "1", "T.3");
                        break;
                    case "T.4":
                        //Pick up dirt
                        if (inventory.contains(Material.DIRT)) {
                            Commands.setStage(player, playerPointer, "1", "T.5");
                            plugin.dialogueManager.runDialogue("T1", player, "1", "T.5");
                        }
                        break;
                    case "T.6":
                        if (scanLocation(playerLocation, -14, 135, -1244, -12, 142, -1243)) {
                            Commands.setStage(player, playerPointer, "1", "T.7");
                            plugin.dialogueManager.runDialogue("T1", player, "1", "T.7");
                        }
                        break;
                    case "T.10":
                        //Craft pickaxe
                        if (inventory.contains(Material.STONE_PICKAXE)) {
                            Commands.setStage(player, playerPointer, "1", "T.12");
                        }
                        break;
                    case "T.12":
                        if (scanLocation(playerLocation, -15, 135, -1228, -12, 142, -1224)) {
                            Commands.setStage(player, playerPointer, "1", "T.13");
                            plugin.dialogueManager.runDialogue("T1", player, "1", "T.13");
                        }
                        break;
                    case "T.14":
                        if (scanLocation(playerLocation, -9, 135, -1222, -5, 142, -1219)) {
                            Commands.setStage(player, playerPointer, "1", "T.15");
                            plugin.dialogueManager.runDialogue("T1", player, "1", "T.15");
                        }
                        break;
                    case "T.16":
                        if (scanLocation(playerLocation, -4, 135, -1221, -2, 142, -1219)) {
                            Commands.setStage(player, playerPointer, "1", "T.17");
                            plugin.dialogueManager.runDialogue("T1", player, "1", "T.17");
                        }
                        break;
                    case "T.18":
                        if (scanLocation(playerLocation, 5, 135, -1230, 7, 142, -1226)) {
                            Commands.setStage(player, playerPointer, "0", "0.0");
                            player.teleport(new Location(player.getWorld(), 160.5, 71, -907.5, 90, 0));
                        }
                        break;


                }
                break;
            case "2":
                switch (objectiveID) {
                    case "T.2":
                        //Just move
                        if (player.getLocation().getX() > -72) {
                            plugin.dialogueManager.runDialogue("T2", player, "2", "T.2");
                            break;
                        }
                        break;
                    case "T.3":
                        if (player.getLocation().getX() > -64) {
                            plugin.dialogueManager.runDialogue("T2", player, "2", "T.4");
                            break;
                        }
                        break;
                    case "T.5":
                        if (player.getLocation().getX() > -60) {
                            plugin.dialogueManager.runDialogue("T2", player, "2", "T.6");
                            break;
                        }
                        break;
                    case "T.7":
                        if (player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 1)) {
                            plugin.dialogueManager.runDialogue("T2", player, "2", "T.8");
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock -50 133 -1163 minecraft:redstone_block");
                            break;
                        }
                        break;
                    case "T.9":
                        if (player.getLocation().getZ() > -1153) {
                            plugin.dialogueManager.runDialogue("T2", player, "2", "T.10");
                            break;
                        }
                        break;
                    case "T.11":
                        if (player.getInventory().containsAtLeast(new ItemStack(Material.OAK_LOG), 1)) {
                            plugin.dialogueManager.runDialogue("T2", player, "2", "T.12");
                            player.teleport(new Location(player.getWorld(), 160.5, 71, -907.5, 90, 0));
                            break;
                        }
                        break;
                    case "3.1.1":
                    case "3.1.2":
                        if (inventory.contains(Material.OAK_LOG)) {
                            Commands.setStage(player, playerPointer, "2", "3.2");
                        }
                        break;
                    case "4.2":
                        //if inventory contains at least one of each of the following : red dye, yellow dye, blue dye, green dye, cyan dye, purple dye, orange dye
                        if (inventory.contains(Material.RED_DYE) && inventory.contains(Material.YELLOW_DYE) && inventory.contains(Material.BLUE_DYE) && inventory.contains(Material.GREEN_DYE) && inventory.contains(Material.CYAN_DYE) && inventory.contains(Material.PURPLE_DYE) && inventory.contains(Material.ORANGE_DYE)) {
                            Commands.setStage(player, playerPointer, "2", "4.3");
                        }
                }
        }


    }

    /**
     * This method is called when a block is placed in the game.
     * If the block being placed is bedrock, the event is cancelled.
     *
     * @param event The BlockPlaceEvent triggered when a block is placed.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        //if the block being places is bedrock, then cancel the event
        if (event.getBlock().getType() == Material.BEDROCK) {
            event.setCancelled(true);
        }
    }

    /**
     * This method checks if a player's location falls within a specified range.
     *
     * @param playerLocation The location of the player.
     * @param x1 The minimum x-coordinate of the range.
     * @param y1 The minimum y-coordinate of the range.
     * @param z1 The minimum z-coordinate of the range.
     * @param x2 The maximum x-coordinate of the range.
     * @param y2 The maximum y-coordinate of the range.
     * @param z2 The maximum z-coordinate of the range.
     * @return true if the player's location is within the specified range, false otherwise.
     */
    private boolean scanLocation(Location playerLocation, int x1, int y1, int z1, int x2, int y2, int z2) {
        if (playerLocation.getX() >= x1 && playerLocation.getX() <= x2 && playerLocation.getY() >= y1 && playerLocation.getY() <= y2 && playerLocation.getZ() >= z1 && playerLocation.getZ() <= z2) {
            return true;
        }
        return false;
    }

    /**
     * This method is called when a player clicks on an inventory. Both player and chest but mainly used for checking when a player clicks on a chest.
     * It performs certain actions based on the clicked inventory and the player's current objective.
     *
     * @param event The InventoryClickEvent triggered when a player clicks on an inventory.
     */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        String objectiveID = playerPointer.getObjectiveID();
        String unit = playerPointer.getUnit();

        if (!objectiveID.equals("0.0")) return;
        if (event.getClickedInventory() == null) return;
        //Select Unit
        String item = event.getCurrentItem().getItemMeta().getDisplayName();


        switch (item) {
            //Unit Select
            case "Unit 1":
                event.setCancelled(true);
                u1SelectionGUI(player);
                break;
            case "Unit 2":
                event.setCancelled(true);
                u2SelectionGUI(player);
                break;
            case "U1 Tutorial":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "1", "T.1");
                break;
            case "U2 Tutorial":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "2", "T.1");
                break;

            //Unit 1 Selection
            case "U1 Lesson 1":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "1", "1.0");
                break;
            case "U1 Lesson 3":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();

                Commands.setStage(player, playerPointer, "1", "3.0");
                break;
            case "U1 Lesson 4":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "1", "4.0");
                break;
            case "U1 Lesson 5":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "1", "5.0");
                break;

            //Unit 2 Selection
            case "U2 Lesson 1":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "2", "2.0");
                break;
            case "U2 Lesson 2":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "2", "3.0");
                break;
            case "U2 Lesson 4":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "2", "4.0");
                break;
            case "U2 Lesson A":
                event.setCancelled(true);
                player.getInventory().clear();
                player.closeInventory();
                Commands.setStage(player, playerPointer, "2", "A.0");
                break;
        }
    }

    /**
     * Handles the PlayerInteractEvent.
     * This method is called when a player interacts with something in the game.
     * It performs different actions based on the player's unit and objective ID.
     *
     * @param event The PlayerInteractEvent object.
     */
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material material = player.getInventory().getItemInMainHand().getType();
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        String objectiveID = playerPointer.getObjectiveID();
        String unit = playerPointer.getUnit();

        if (unit.equals("1")) {
            switch (objectiveID) {
                case "1.2":
                    if (material.equals(Material.WRITTEN_BOOK)) {
                        Commands.setStage(player, playerPointer, "1", "1.3");
                    }
                    break;
                case "1.15":
                    if (player.getInventory().contains(Material.OAK_PLANKS, 20) && player.getInventory().contains(Material.STICK, 10) && player.getInventory().contains(Material.STONE_AXE, 4)) {
                        Commands.setStage(player, playerPointer, "1", "1.16");
                    }
                    break;
            }
            if (objectiveID.charAt(0) == 'T') {
                if (material.equals(Material.BOOK)) {
                    System.out.println(objectiveID);
                    plugin.dialogueManager.runDialogue("T1", player, "1", objectiveID);
                }
            }
        } else if (unit.equals("2")) {
            switch (objectiveID) {
                case "4.3":
                    if (material.equals(Material.WRITTEN_BOOK)) {
                        Commands.setStage(player, playerPointer, "2", "4.4");
                    }
                    break;
            }
            if (objectiveID.charAt(0) == 'T') {
                if (material.equals(Material.BOOK)) {
                    System.out.println(objectiveID);
                    plugin.dialogueManager.runDialogue("T1", player, "2", objectiveID);
                }
            }
        } else if (objectiveID.equals("0.0")) {
            if (material.equals(Material.BOOK)) {
                openSelectGUI(player);
            }
        }
    }

    /**
     * Opens the Select Unit GUI for the player.
     * This GUI allows the player to select their unit.
     *
     * @param player The player who will see the GUI.
     */
    private void openSelectGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Select Unit");

        ItemStack unit1 = new ItemStack(Material.CHEST);
        ItemMeta unit1Meta = unit1.getItemMeta();
        unit1Meta.setDisplayName("Unit 1");
        unit1.setItemMeta(unit1Meta);

        ItemStack unit2 = new ItemStack(Material.HOPPER);
        ItemMeta unit2Meta = unit2.getItemMeta();
        unit2Meta.setDisplayName("Unit 2");
        unit2.setItemMeta(unit2Meta);


        inv.setItem(3, unit1);
        inv.setItem(5, unit2);

        player.openInventory(inv);
    }

    /**
     * Opens the Unit 1 selection GUI for the player.
     * This GUI allows the player to select lessons for Unit 1.
     *
     * @param player The player who will see the GUI.
     */
    void u1SelectionGUI(Player player) {
        Inventory inventory = plugin.getServer().createInventory(null, 9, "Unit 1");

        ItemStack lesson1 = new ItemStack(Material.CRAFTING_TABLE);
        ItemStack lesson3 = new ItemStack(Material.OAK_PLANKS);
        ItemStack lesson4 = new ItemStack(Material.SMOOTH_STONE);
        ItemStack lesson5 = new ItemStack(Material.RED_CONCRETE);

        ItemStack u1Tutorial = new ItemStack(Material.BOOK);
        ItemMeta u1TutorialMeta = u1Tutorial.getItemMeta();
        u1TutorialMeta.setDisplayName("U1 Tutorial");
        u1Tutorial.setItemMeta(u1TutorialMeta);
        inventory.setItem(0, u1Tutorial);

        ItemMeta itemMeta = lesson1.getItemMeta();
        itemMeta.setDisplayName("U1 Lesson 1");
        lesson1.setItemMeta(itemMeta);
        inventory.setItem(1, lesson1);

        itemMeta = lesson3.getItemMeta();
        itemMeta.setDisplayName("U1 Lesson 3");
        lesson3.setItemMeta(itemMeta);
        inventory.setItem(3, lesson3);

        itemMeta = lesson4.getItemMeta();
        itemMeta.setDisplayName("U1 Lesson 4");
        lesson4.setItemMeta(itemMeta);
        inventory.setItem(5, lesson4);

        itemMeta = lesson5.getItemMeta();
        itemMeta.setDisplayName("U1 Lesson 5");
        lesson5.setItemMeta(itemMeta);
        inventory.setItem(7, lesson5);

        player.openInventory(inventory);
    }

    /**
     * Opens the Unit 2 selection GUI for the player.
     * This GUI allows the player to select lessons for Unit 2.
     *
     * @param player The player who will see the GUI.
     */
    void u2SelectionGUI(Player player) {
        Inventory inventory = plugin.getServer().createInventory(null, 9, "Unit 2");

        ItemStack lesson1 = new ItemStack(Material.POLISHED_ANDESITE);
        ItemStack lesson2 = new ItemStack(Material.BRICKS);
        ItemStack lesson4 = new ItemStack(Material.HOPPER);
        ItemStack lessonA = new ItemStack(Material.BLUE_GLAZED_TERRACOTTA);

        ItemStack u2Tutorial = new ItemStack(Material.BOOK);
        ItemMeta u2TutorialMeta = u2Tutorial.getItemMeta();
        u2TutorialMeta.setDisplayName("U2 Tutorial");
        u2Tutorial.setItemMeta(u2TutorialMeta);
        inventory.setItem(0, u2Tutorial);

        ItemMeta itemMeta = lesson1.getItemMeta();
        itemMeta.setDisplayName("U2 Lesson 1");
        lesson1.setItemMeta(itemMeta);
        inventory.setItem(2, lesson1);

        itemMeta = lesson2.getItemMeta();
        itemMeta.setDisplayName("U2 Lesson 2");
        lesson2.setItemMeta(itemMeta);
        inventory.setItem(4, lesson2);

        itemMeta = lesson4.getItemMeta();
        itemMeta.setDisplayName("U2 Lesson 4");
        lesson4.setItemMeta(itemMeta);
        inventory.setItem(6, lesson4);

        itemMeta = lessonA.getItemMeta();
        itemMeta.setDisplayName("U2 Lesson A");
        lessonA.setItemMeta(itemMeta);
        inventory.setItem(8, lessonA);


        player.openInventory(inventory);
    }

    private void quickFrameEval(Player player) {
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        int stick = 0;
        int plank = 0;
        for (GlowItemFrame itemFrame : u2l1ItemFrames) {
            if (itemFrame.getItem().getType().equals(Material.STICK)) {
                stick++;
            }
            if (itemFrame.getItem().getType().equals(Material.OAK_PLANKS)) {
                plank++;
            }
        }
        if (stick == 1 && plank == 1) {
            Commands.setStage(player, playerPointer, "2", "2.4");
        }
    }

    /**
     * This method is called when an inventory is opened.
     * It checks the title of the inventory view and performs certain actions based on the title.
     *
     * @param event The InventoryOpenEvent that triggered this method.
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        InventoryView inventoryView = event.getView();
        Inventory inventory = event.getInventory();

        if (inventoryView.getTitle().equals("Chest")) {
            if (!chests.contains(inventoryView.getTopInventory().getHolder())) {
                //System.out.println("Chest Opened");
                chests.add(inventoryView.getTopInventory().getHolder());
            }
        }
        if (inventoryView.getTitle().equals("Large Chest")) {
            DoubleChest doubleChest = (DoubleChest) inventoryView.getTopInventory().getHolder();
            if (!chests.contains(doubleChest.getLeftSide()) && !chests.contains(doubleChest.getRightSide())) {
                //System.out.println("Large Chest Opened");
                chests.add(doubleChest.getLeftSide());
                chests.add(doubleChest.getRightSide());
            }
        }
    }

    /**
     * This method is called when an inventory is closed.
     * It performs various actions based on the title of the inventory view and the player's objective.
     *
     * @param event The InventoryCloseEvent that triggered this method.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        InventoryView inventoryView = event.getView();


        evalChests((Player) event.getPlayer());

        updatedChestGet(playerPointer, event);

        updatedChestEval(playerPointer, event);
        if(inventoryView.getTitle().equals("Chest")) {
            if (playerPointer.getUnit().equals("1") && playerPointer.getObjectiveID().equals("T.8")) {
                Commands.setStage(player, playerPointer, "1", "T.9");
                plugin.dialogueManager.runDialogue("T1", player, "1", "T.9");
            }
        }

    }

    /**
     * This method updates the chest get based on the player's unit and objectiveID.
     *
     * @param playerPointer The PlayerPointer object associated with the player.
     * @param event         The InventoryCloseEvent that triggered this method.
     */
    private void updatedChestGet(PlayerPointer playerPointer, InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String unit = playerPointer.getUnit();
        String objectiveID = playerPointer.getObjectiveID();

        //Unit 1 Lesson 3
        Location woolShip = new Location(player.getWorld(), 118, 72, -877);
        Location stoneShip = new Location(player.getWorld(), 118, 72, -875);
        Location woodShip = new Location(player.getWorld(), 118, 72, -873);

        Location woodSort = new Location(player.getWorld(), 122, 72, -873);
        Location stoneSort = new Location(player.getWorld(), 122, 72, -875);
        Location woolSort = new Location(player.getWorld(), 122, 72, -877);
        Location otherSort = new Location(player.getWorld(), 122, 72, -879);

        //Unit 1 Lesson 4
        Location smelterShip = new Location(player.getWorld(), 127, 72, -813);
        Location toolShip = new Location(player.getWorld(), 125, 72, -813);
        Location bookShip = new Location(player.getWorld(), 123, 72, -813);

        //Unit 1 Lesson 5
        Location outgoing = new Location(player.getWorld(), 131, 71, -821);

        //Unit 2 Lesson 2
        Location pedastal = new Location(player.getWorld(), 125, 72, -875);

        switch (unit) {
            case "1":
                switch (objectiveID) {
                    case "3.2":
                        if (event.getInventory().getLocation().equals(woodSort)) {
                            u1l3sorters[0] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(stoneSort)) {
                            u1l3sorters[1] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(woolSort)) {
                            u1l3sorters[2] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(otherSort)) {
                            u1l3sorters[3] = event.getInventory();
                        }
                        break;
                    case "3.3":
                        if (event.getInventory().getLocation().equals(woolShip)) {
                            u1l3orders[0] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(stoneShip)) {
                            u1l3orders[1] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(woodShip)) {
                            u1l3orders[2] = event.getInventory();
                        }

                        break;
                    case "4.5":
                        if (event.getInventory().getLocation().equals(smelterShip)) {
                            u1l4orders[0] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(toolShip)) {
                            u1l4orders[1] = event.getInventory();
                        }
                        if (event.getInventory().getLocation().equals(bookShip)) {
                            u1l4orders[2] = event.getInventory();
                        }

                        break;
                    case "5.13":
                        if (event.getInventory().getLocation().equals(outgoing)) {
                            System.out.println("Outgoing Chest Set");
                            u1l5Outgoing[0] = event.getInventory();
                        }


                }
                break;
            case "2":
                switch (objectiveID) {
                    case "2.2":
                    case "2.3":
                        if (event.getInventory().getLocation().equals(pedastal)) {
                            u2l2Pedastal = event.getInventory();
                        }
                        break;
                }
                break;
        }

    }

    /**
     * This method is called when a player closes an inventory and evaluates the contents of the inventory to determine if certain items are present.
     * If the required items are found in the inventory, the method updates the player's stage.
     *
     * @param playerPointer The player pointer object containing information about the player.
     * @param event         The InventoryCloseEvent triggered when the player closes the inventory.
     */
    private void updatedChestEval(PlayerPointer playerPointer, InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        String objectiveID = playerPointer.getObjectiveID();
        String unit = playerPointer.getUnit();


        if (unit.equals("1")) {
            if (objectiveID.equals("3.2")) {
                int completed = 0;
                //Wood Chest
                ItemStack spruceLog = new ItemStack(Material.SPRUCE_LOG);   //x5
                ItemStack oakLog = new ItemStack(Material.OAK_LOG);         //x5
                ItemStack birchLog = new ItemStack(Material.BIRCH_LOG);     //x5

                //Stone Chest
                ItemStack cobblestone = new ItemStack(Material.COBBLESTONE); //x5
                ItemStack deepslate = new ItemStack(Material.DEEPSLATE);     //x1
                ItemStack blackstone = new ItemStack(Material.BLACKSTONE);   //x5

                //Wool Chest
                ItemStack wool = new ItemStack(Material.WHITE_WOOL);         //x10
                ItemStack greyWool = new ItemStack(Material.GRAY_WOOL);//x2
                ItemStack blueWool = new ItemStack(Material.BLUE_WOOL);      //x2
                ItemStack orangeWool = new ItemStack(Material.ORANGE_WOOL);  //x2

                //Other Chest
                ItemStack appleItem = new ItemStack(Material.APPLE); //x1


                if (u1l3sorters[0] != null) {
                    inventory = u1l3sorters[0];
                    if (inventory.containsAtLeast(spruceLog, 5) && inventory.containsAtLeast(oakLog, 5) && inventory.containsAtLeast(birchLog, 5)) {
                        System.out.println("Wood Sorted");
                        completed++;
                    }
                }
                if (u1l3sorters[1] != null) {
                    inventory = u1l3sorters[1];
                    if (inventory.containsAtLeast(cobblestone, 5) && inventory.containsAtLeast(deepslate, 1) && inventory.containsAtLeast(blackstone, 5)) {
                        System.out.println("Stone Sorted");
                        completed++;
                    }
                }
                if (u1l3sorters[2] != null) {
                    inventory = u1l3sorters[2];
                    if (inventory.containsAtLeast(wool, 10) && inventory.containsAtLeast(greyWool, 2) && inventory.containsAtLeast(blueWool, 2) && inventory.containsAtLeast(orangeWool, 2)) {
                        System.out.println("Wool Sorted");
                        completed++;
                    }
                }
                if (u1l3sorters[3] != null) {
                    inventory = u1l3sorters[3];
                    if (inventory.containsAtLeast(appleItem, 1)) {
                        System.out.println("Other Sorted");
                        completed++;
                    }
                }


                if (completed == 4) {
                    Commands.setStage(player, playerPointer, "1", "3.3");
                }

            }
            if (objectiveID.equals("3.3")) {
                int order = 0;

                ItemStack woolItem1 = new ItemStack(Material.WHITE_BED);
                ItemStack woolItem2 = new ItemStack(Material.LIGHT_GRAY_BED);
                ItemStack woolItem3 = new ItemStack(Material.GRAY_BED);

                ItemStack stoneItem1 = new ItemStack(Material.WOODEN_AXE);
                ItemStack stoneItem2 = new ItemStack(Material.STONE_SHOVEL);
                ItemStack stoneItem3 = new ItemStack(Material.STONE_PICKAXE);

                ItemStack woodItem1 = new ItemStack(Material.OAK_PLANKS);
                ItemStack woodItem2 = new ItemStack(Material.BIRCH_PLANKS);
                ItemStack woodItem3 = new ItemStack(Material.STICK);

                if (u1l3orders[0] != null) {
                    inventory = u1l3orders[0];
                    if (inventory.containsAtLeast(woolItem1, 2) && inventory.containsAtLeast(woolItem2, 1) && inventory.containsAtLeast(woolItem3, 1)) {
                        System.out.println("Wool Ship Complete");
                        order++;
                    }
                }

                if (u1l3orders[1] != null) {
                    inventory = u1l3orders[1];
                    if (inventory.containsAtLeast(stoneItem1, 5) && inventory.containsAtLeast(stoneItem2, 2) && inventory.containsAtLeast(stoneItem3, 3)) {
                        System.out.println("Tool Ship Complete");
                        order++;
                    }
                }

                if (u1l3orders[2] != null) {
                    inventory = u1l3orders[2];
                    if (inventory.containsAtLeast(woodItem1, 10) && inventory.containsAtLeast(woodItem2, 12) && inventory.containsAtLeast(woodItem3, 20)) {
                        System.out.println("Wood Ship Complete");
                        order++;
                    }
                }

                if (order == 3) {
                    Commands.setStage(player, playerPointer, "1", "3.4");
                }
            }
            if (objectiveID.equals("4.5")) {
                System.out.println("4.4");
                int order = 0;
                ItemStack smelter1 = new ItemStack(Material.CHARCOAL);
                ItemStack smelter2 = new ItemStack(Material.IRON_INGOT);
                ItemStack smelter3 = new ItemStack(Material.COPPER_INGOT);
                ItemStack smelter4 = new ItemStack(Material.GLASS);
                ItemStack smelter5 = new ItemStack(Material.SMOOTH_STONE);

                ItemStack tool1 = new ItemStack(Material.IRON_AXE);
                ItemStack tool2 = new ItemStack(Material.IRON_PICKAXE);
                ItemStack tool3 = new ItemStack(Material.TORCH);
                ItemStack tool4 = new ItemStack(Material.SHEARS);

                ItemStack book1 = new ItemStack(Material.BOOK);
                ItemStack book2 = new ItemStack(Material.BOOKSHELF);

                if (u1l4orders[0] != null) {
                    inventory = u1l4orders[0];
                    if (inventory.containsAtLeast(smelter1, 20) && inventory.containsAtLeast(smelter2, 4) && inventory.containsAtLeast(smelter3, 5) && inventory.containsAtLeast(smelter4, 10) && inventory.containsAtLeast(smelter5, 11)) {
                        System.out.println("Smelter Ship Complete");
                        order++;
                    }
                }
                if (u1l4orders[1] != null) {
                    inventory = u1l4orders[1];
                    if (inventory.containsAtLeast(tool1, 6) && inventory.containsAtLeast(tool2, 6) && inventory.containsAtLeast(tool3, 10) && inventory.containsAtLeast(tool4, 3)) {
                        System.out.println("Tool Ship Complete");
                        order++;
                    }
                }
                if (u1l4orders[2] != null) {
                    inventory = u1l4orders[2];
                    if (inventory.containsAtLeast(book1, 7) && inventory.containsAtLeast(book2, 3)) {
                        System.out.println("Book Ship Complete");
                        order++;
                    }
                }

                if (order == 3) {
                    Commands.setStage(player, playerPointer, "1", "4.6");
                }
            }
            if (objectiveID.equals("5.13")) {
                if (u1l5Outgoing[0] != null) {
                    inventory = u1l5Outgoing[0];
                    //Black bed x3, Blue Wool x2, Green Stained Glass x3, white candle x3, white bed x2
                    ItemStack blackBed = new ItemStack(Material.BLACK_BED);
                    ItemStack blueWool = new ItemStack(Material.BLUE_WOOL);
                    ItemStack greenGlass = new ItemStack(Material.GREEN_STAINED_GLASS);
                    ItemStack whiteCandle = new ItemStack(Material.WHITE_CANDLE);
                    ItemStack whiteBed = new ItemStack(Material.WHITE_BED);

                    int orders = 0;
                    if (inventory.containsAtLeast(blackBed, 3)) {
                        System.out.println("Black Bed");
                        orders++;
                    }
                    if (inventory.containsAtLeast(blueWool, 2)) {
                        System.out.println("Blue Wool");
                        orders++;
                    }
                    if (inventory.containsAtLeast(greenGlass, 3)) {
                        System.out.println("Green Glass");
                        orders++;
                    }
                    if (inventory.containsAtLeast(whiteCandle, 3)) {
                        System.out.println("White Candle");
                        orders++;
                    }
                    if (inventory.containsAtLeast(whiteBed, 2)) {
                        System.out.println("White Bed");
                        orders++;
                    }

                    if (orders == 5) {
                        Commands.setStage(player, playerPointer, "1", "5.14");
                    }


                }

            }
        } else if (unit.equals("2")) {
            if (objectiveID.equals("2.2") || objectiveID.equals("2.3")) {
                ItemStack plank = new ItemStack(Material.OAK_PLANKS);
                ItemStack stick = new ItemStack(Material.STICK);

                inventory = u2l2Pedastal;

                boolean hasPlank = false;
                boolean hasStick = false;

                for (ItemStack item : inventory.getContents()) {
                    if (item != null) {
                        if (!item.equals(plank) && !item.equals(stick)) {
                            return;
                        }
                        if (item.equals(plank)) {
                            hasPlank = true;
                        }
                        if (item.equals(stick)) {
                            hasStick = true;
                        }
                    }
                }

                System.out.println("Plank: " + hasPlank);
                System.out.println("Stick: " + hasStick);

                if (hasPlank && hasStick) {
                    Commands.setStage(player, playerPointer, "2", "2.4");
                }
            }
        }
    }

    /**
     * This method evaluates the chests for a given player based on their unit and objective ID.
     * It checks the contents of the chests and performs certain actions if specific items are found.
     *
     * @param player The player for whom the chests are being evaluated.
     */
    static void evalChests(Player player) {
        PlayerPointer playerPointer = HuMInLabPlugin.objectiveStorage.getPlayerPointer(player);
        String unit = playerPointer.getUnit();
        String objectiveID = playerPointer.getObjectiveID();

        switch (unit) {
            case "1":
                switch (objectiveID) {
                    case "1.16":
                        int order = 0;
                        for (InventoryHolder chest : chests) {
                            if (chest.getInventory().containsAtLeast(new ItemStack(Material.OAK_PLANKS), 20)) {
                                order += 1;
                                System.out.println("Planks detected");
                            }
                            if (chest.getInventory().containsAtLeast(new ItemStack(Material.STICK), 10)) {
                                order += 1;
                                System.out.println("Sticks detected");
                            }
                            if (chest.getInventory().containsAtLeast(new ItemStack(Material.STONE_AXE), 4)) {
                                order += 1;
                                System.out.println("Axes detected");
                            }
                            if (order == 3) {
                                Commands.setStage(player, playerPointer, "1", "1.17");
                            }
                            System.out.println("Order: " + order);
                        }
                        break;
                        //TODO THIS IS AN OLD VERSION OF LESSON 4
//                    case "4.5":
//                        order = 0;
//                        //Order 1
//                        order1 = lesson4Chests.get(0);
//                        order1Item1 = new ItemStack(Material.CHARCOAL, 1);
//                        order1Item2 = new ItemStack(Material.IRON_INGOT, 1);
//                        order1Item3 = new ItemStack(Material.COPPER_INGOT, 1);
//                        ItemStack order1Item4 = new ItemStack(Material.GLASS, 1);
//                        ItemStack order1Item5 = new ItemStack(Material.SMOOTH_STONE, 1);
//
//                        if(order1.getBlockInventory().containsAtLeast(order1Item1, 20) && order1.getBlockInventory().containsAtLeast(order1Item2, 4) && order1.getBlockInventory().containsAtLeast(order1Item3, 5) && order1.getBlockInventory().containsAtLeast(order1Item4, 10) && order1.getBlockInventory().containsAtLeast(order1Item5, 11)){
//                            order++;
//                        }
//
//                        //Order 2
//                        order2 = lesson4Chests.get(1);
//                        order1Item1 = new ItemStack(Material.IRON_AXE, 1);
//                        order1Item2 = new ItemStack(Material.IRON_PICKAXE, 1);
//                        order1Item3 = new ItemStack(Material.TORCH, 1);
//                        order1Item4 = new ItemStack(Material.SHEARS, 1);
//
//                        if(order2.getBlockInventory().containsAtLeast(order1Item1, 6) && order2.getBlockInventory().containsAtLeast(order1Item2, 6) && order2.getBlockInventory().containsAtLeast(order1Item3, 10) && order2.getBlockInventory().containsAtLeast(order1Item4, 3)){
//                            order++;
//                        }
//
//                        //Order 3
//                        order3 = lesson4Chests.get(2);
//                        order1Item1 = new ItemStack(Material.BOOK, 1);
//                        order1Item2 = new ItemStack(Material.BOOKSHELF, 1);
//
//                        if(order3.getBlockInventory().containsAtLeast(order1Item1, 7) && order3.getBlockInventory().containsAtLeast(order1Item2, 3)){
//                            order++;
//                        }
//
//                        if (order == 3){
//                            player.sendMessage("All Orders Complete");
//                            Commands.setStage(player, playerPointer, "1", "4.6");
//                        }
                }
                break;
            case "2":
                switch (objectiveID) {
                    case "3.3":
                        //TODO
                        //get chest at 149 72 -839
                        Chest u2Lesson3SantiChest = (Chest) player.getWorld().getBlockAt(149, 72, -839).getState();
                        Inventory chestInv = u2Lesson3SantiChest.getInventory();
                        int i = 0;

                        for (ItemStack item : chestInv.getContents()) {
                            if (item != null) {
                                System.out.println(item.getItemMeta().getDisplayName());
                                if (item.getItemMeta().getDisplayName().equals("Contraption Block 0")) {
                                    i++;
                                } else if (item.getItemMeta().getDisplayName().equals("Contraption Block 1")) {
                                    i++;
                                }
                            }
                        }

                        if (i == 2) {
                            Commands.setStage(player, playerPointer, "2", "3.4");
                        }
                        break;
                    case "4.5":
                        //TODO
                        //get chest at 149 72 -839
                        u2Lesson3SantiChest = (Chest) player.getWorld().getBlockAt(149, 72, -839).getState();
                        chestInv = u2Lesson3SantiChest.getInventory();


                        int chestCheck = 0;
                        for (ItemStack item : chestInv.getContents()) {
                            if (item != null) {
                                if (item.getType() == Material.BLUE_GLAZED_TERRACOTTA) {
                                    chestCheck++;
                                    if (chestCheck == 3) {
                                        break;
                                    }
                                }
                            }
                        }

                        if (chestCheck == 3) {
                            Commands.setStage(player, playerPointer, "2", "4.6");
                        }
                    case "A.1":
                    case "A.1.0":
                        u2Lesson3SantiChest = (Chest) player.getWorld().getBlockAt(149, 72, -839).getState();
                        chestInv = u2Lesson3SantiChest.getInventory();

                        //check to see if chest contains
                        //-64 Oak Planks
                        //-64 Sticks
                        //-64 Oak Fences
                        //-64 Oak fence gates
                        //and at least 4 contraptions

                        int order = 0;
                        int contraptions = 0;
                        for (ItemStack item : chestInv.getContents()) {
                            if (item != null) {
                                if (item.getType() == Material.OAK_PLANKS) {
                                    if (item.getAmount() >= 64) {
                                        order++;
                                    }
                                } else if (item.getType() == Material.STICK) {
                                    if (item.getAmount() >= 64) {
                                        order++;
                                    }
                                } else if (item.getType() == Material.OAK_FENCE) {
                                    if (item.getAmount() >= 64) {
                                        order++;
                                    }
                                } else if (item.getType() == Material.OAK_FENCE_GATE) {
                                    if (item.getAmount() >= 64) {
                                        order++;
                                    }
                                } else if (item.getType() == Material.BLUE_GLAZED_TERRACOTTA) {
                                    contraptions++;
                                }
                            }
                        }

                        System.out.println("Order: " + order + " Contraptions: " + contraptions);

                        if (order == 4 && contraptions >= 4) {
                            Commands.setStage(player, playerPointer, "2", "A.2");
                        }

                        break;
                }
        }
        return;
    }

}
