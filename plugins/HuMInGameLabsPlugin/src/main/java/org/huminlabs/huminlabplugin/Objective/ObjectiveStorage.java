package org.huminlabs.huminlabplugin.Objective;

import com.google.gson.Gson;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.huminlabs.huminlabplugin.Commands;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;



public class ObjectiveStorage {
    private final HuMInLabPlugin plugin;
    private static BossBar bossBar;
    private static ArrayList<Objective> objectives = new ArrayList<>();
    private static ArrayList<PlayerPointer> pointers = new ArrayList<>();

    private static ArrayList<File> schematicFiles = new ArrayList<>();
    private static WorldChange[] worldChanges = new WorldChange[0];

    //Twin is in DialogueManager
    private static String prefix = "/static";
    //  HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath();
    //  "/static"

    public static ArrayList<PlayerPointer> getPointers() {
        return pointers;
    }

    // /static
    public ObjectiveStorage(HuMInLabPlugin plugin) {
        this.plugin = plugin;
        bossBar = plugin.getServer().createBossBar("HuMIn Game Labs", org.bukkit.boss.BarColor.BLUE, org.bukkit.boss.BarStyle.SOLID);
        try {
            loadObjectives();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            loadPlayerPointers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            loadSchems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the objectives from JSON files for Unit 1 and Unit 2, and the world changes from WorldChanges.json.
     * Sets the unit property of each objective to the corresponding unit.
     *
     * @throws IOException If there is an error reading the JSON files.
     */

    public static void loadObjectives() throws IOException {
        Gson gson = new Gson();
        // Load Unit 1 Objectives
        File file = new File(prefix + "/Objectives/Unit_1_Objectives.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Objective[] o = gson.fromJson(reader, Objective[].class);
            for (Objective objective : o) {
                objective.setUnit("1");
            }
            objectives = new ArrayList<>(Arrays.asList(o));
            System.out.println("Unit 1 loaded: " + o.length);

        } else {
            System.out.println("Unit 1 Objectives not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }

        // Load Unit 2 Objectives
        file = new File(prefix + "/Objectives/Unit_2_Objectives.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Objective[] o = gson.fromJson(reader, Objective[].class);
            for (Objective objective : o) {
                objective.setUnit("2");
            }
            objectives.addAll(new ArrayList<>(Arrays.asList(o)));
            System.out.println("Unit 2 loaded: " + o.length);
        } else {
            System.out.println("Unit 2 Objectives not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }

        // Load World Changes
        File worldChanges = new File(prefix + "/Objectives/WorldChanges.json");
        if(worldChanges.exists()) {
            Reader reader = new FileReader(worldChanges);
            WorldChange[] w = gson.fromJson(reader, WorldChange[].class);
            for(WorldChange worldChange : w) {
                System.out.println(worldChange.getCommandString());
            }
            ObjectiveStorage.worldChanges = w;
            System.out.println("World Changes loaded: " + w.length);
        } else {
            System.out.println("World Changes not found!");
            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }
    }

    /**
     * Retrieves an objective based on its ID and unit.
     *
     * @param id   The ID of the objective.
     * @param unit The unit of the objective.
     * @return The objective with the specified ID and unit, or null if not found.
     */
    public static Objective getObjective(String id, String unit) {
        for (Objective objective : objectives) {
            if (objective.getId().equals(id) && objective.getUnit().equals(unit)) {
                return objective;
            }
        }
        return null;
    }

    /**
     * Loads the schematic files from the Schematics folder.
     *
     * @throws IOException If there is an error reading the schematic files.
     */
    public static void loadSchems() throws IOException {
        File file = new File(prefix + "/Schematics");
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                schematicFiles.add(f);
            }
        }
    }

    /**
     * Adds a player pointer for the specified player.
     * If a pointer already exists for the player, it updates the objective of the pointer.
     *
     * @param player The player to add the pointer for.
     * @return The added or updated player pointer.
     * @throws IOException If there is an error saving the player pointers.
     */

    public static PlayerPointer addPlayerPointer(Player player) throws IOException {

        for (PlayerPointer p : pointers) {
            if (p.getUUID().equals(player.getUniqueId().toString())) {
                p.setObjective(p.getUnit(), p.getObjectiveID());
                return p;
            }
        }
        PlayerPointer pointer = new PlayerPointer(player.getUniqueId().toString());
        pointers.add(pointer);
        pointer.setObjective(pointer.getUnit(), pointer.getObjectiveID());

        savePlayerPointers();
        return pointer;
    }

    /**
     * Loads the player pointers from the player pointers file.
     *
     * @throws IOException If there is an error reading the player pointers file.
     */
    public static void loadPlayerPointers() throws IOException {
        Gson gson = new Gson();
        File file = new File("VOID : NO MORE PLAYERPOINTER FILE FOR A REASON");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            PlayerPointer[] p = gson.fromJson(reader, PlayerPointer[].class);
            pointers = new ArrayList<>(Arrays.asList(p));
            System.out.println("PlayerPointers loaded: " + p.length);

        } else {
//            System.out.println("PlayerPointers file not found!");
//            System.out.println(HuMInLabPlugin.getPlugin().getDataFolder().getAbsolutePath());
        }
    }

    /**
     * Saves the player pointers to a JSON file.
     *
     * @throws IOException if an I/O error occurs while writing the file.
     */

    //TODO Only save specific playerpointer
    public static void savePlayerPointers() throws IOException {
        Gson gson = new Gson();
        File file = new File(prefix + "/PlayerData/PlayerPointers.json");
        file.getParentFile().mkdirs();
        file.createNewFile();

        Writer writer = new FileWriter(file, false);
        gson.toJson(pointers, writer);
        writer.flush();
        writer.close();
        System.out.println("PlayerPointers saved!");

    }

    /**
     * Retrieves the player pointer for the given player.
     *
     * @param player the player object.
     * @return the player pointer associated with the player.
     */
    public static PlayerPointer getPlayerPointer(Player player) {

        String uuid = player.getUniqueId().toString();
        for (PlayerPointer pointer : pointers) {
            if (pointer.getUUID().equals(uuid)) {
                return pointer;
            }
        }
//        try {
//            return addPlayerPointer(player);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
        System.out.println("HuMInPlugin Error: PlayerPointer not found for " + player.getName() + " (" + uuid + ")");
        PlayerPointer pointer = new PlayerPointer(uuid);
        pointers.add(pointer);
        return pointer;
    }

    /**
     * Adds or updates a player pointer.
     *
     * @param pointer the player pointer to add or update.
     */
    public static void addPlayerPointer(PlayerPointer pointer) {
        for (PlayerPointer p : pointers) {
            if (p.getUUID().equals(pointer.getUUID())) {
                pointers.remove(p);
                break;
            }
        }
        pointers.add(pointer);
    }

    /**
     * Sets the next objective for the player.
     *
     * @param player the player object.
     */
    public static void setNextObjective(Player player) {
        PlayerPointer pointer = getPlayerPointer(player);
        if (pointer != null) {
            for (int i = 0; i < objectives.size(); i++) {
                if (objectives.get(i).getId().equals(pointer.getObjectiveID()) && objectives.get(i).getUnit().equals(pointer.getUnit())) {
                    if (i + 1 < objectives.size()) {
                        pointer.setObjective(objectives.get(i + 1).getUnit(), objectives.get(i + 1).getId());
                        try {
                            savePlayerPointers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Sets the previous objective for the player.
     *
     * @param player the player object.
     */
    public static void setPrevObjective(Player player) {
        PlayerPointer pointer = getPlayerPointer(player);
        if (pointer != null) {
            for (int i = 0; i < objectives.size(); i++) {
                if (objectives.get(i).getId().equals(pointer.getObjectiveID()) && objectives.get(i).getUnit().equals(pointer.getUnit())) {
                    if (i - 1 >= 0) {
                        pointer.setObjective(objectives.get(i - 1).getUnit(), objectives.get(i - 1).getId());
                        try {
                            savePlayerPointers();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Updates the objective for the player.
     *
     * @param player the player object.
     */
    public static void updateObjective(Player player) {
        PlayerPointer playerPointer = getPlayerPointer(player);
        Objective objective = getObjective(playerPointer.getObjectiveID(), playerPointer.getUnit());
        String unit = playerPointer.getUnit();
        String ID = playerPointer.getObjectiveID();


        if (playerPointer == null) {
            try {
                addPlayerPointer(player);
            } catch (Exception e) {
                System.out.println("Player pointer not found!");
            }
        }
        if (ID.equals("0.0")) {
            endLessonProcedure(player);
        } else if (unit != null && ID != null) {
            if (unit.equals("1") && ID.equals("T.1")) {
                u1TutorialProcedure(player);
            } else if (unit.equals("2") && ID.equals("T.1")) {
                u2TutorialProcedure(player);
            } else if (unit.equals("2")) {
                if (ID.equals("A.1.1")) {
                    //give player logs and change to 5.1.0
                    player.getInventory().addItem(new ItemStack(Material.OAK_LOG, 16));
                    Commands.setStage(player, playerPointer, "2", "A.1.0");
                } else if (ID.equals("A.1.2")) {
                    player.getInventory().addItem(new ItemStack(Material.REDSTONE, 5));
                    Commands.setStage(player, playerPointer, "2", "A.1.0");
                }
            }

            handleBossBar(player, objective.getObjective());
            handleCompass(player, objective.getLocation());
            playSound(player);

            handleWorldChange(unit, ID, player);
            handleObjectiveItems(unit, ID, player);

            handleActorPositions(unit, ID);

        }
    }

    /**
     * Performs the tutorial procedure for the first tutorial unit.
     *
     * @param player The player for whom the tutorial procedure is performed.
     */
    private static void u1TutorialProcedure(Player player) {
        player.getInventory().clear();
        player.teleport(new Location(player.getWorld(), -30.5, 139, -1246.5));
        System.out.println("Teleported player to tutorial spawn");
        resetWorld();
        player.getInventory().addItem(new ItemStack(Material.BOOK));
        HuMInLabPlugin.dialogueManager.runDialogue("T1", player, "1", "T.1");
    }

    /**
     * Performs the tutorial procedure for the second tutorial unit.
     *
     * @param player The player for whom the tutorial procedure is performed.
     */
    private static void u2TutorialProcedure(Player player) {
        player.getInventory().clear();
        player.teleport(new Location(player.getWorld(), -76.5, 134, -1162.5));
        System.out.println("Teleported player to tutorial 2 spawn");
        resetWorld();
        player.getInventory().addItem(new ItemStack(Material.BOOK));
        HuMInLabPlugin.dialogueManager.runDialogue("T2", player, "2", "T.1");
    }

    /**
     * Performs the procedure to end a lesson.
     *
     * @param player The player for whom the end lesson procedure is performed.
     */
    private static void endLessonProcedure(Player player) {
        player.getInventory().clear();
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        meta.setDisplayName("Right Click for Lesson Menu");
        bossBar.setVisible(false);
        resetWorld();
    }

    /**
     * Handles the boss bar for a player and sets the task title and progress.
     *
     * @param player The player for whom the boss bar is handled.
     * @param task The title of the task to be displayed on the boss bar.
     */
    static void handleBossBar(Player player, String task) {
        bossBar.setTitle(task);
        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
        bossBar.removeAll();
        bossBar.addPlayer(player);
    }

    /**
     * Plays a sound little ding sound for a player.
     *
     * @param player The player for whom the sound is played.
     */
    static void playSound(Player player) {
        player.playSound(player.getLocation(), "minecraft:block.note_block.pling", 1, 1);
    }

    /**
     * Handles the compass for a player and sets the compass target location for objectives.
     *
     * @param player The player for whom the compass is handled.
     * @param location An array containing the x and z coordinates of the target location.
     */
    static void handleCompass(Player player, int[] location) {
        //if player has compass
        if (location[0] == 0 && location[1] == 0) {
            return;
        }
        if (!player.getInventory().contains(Material.COMPASS)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
        player.setCompassTarget(new Location(player.getWorld(), location[0], 70, location[1]));
    }

    /**
     * Handles a world change based on the unit and ID provided.
     *
     * @param unit The unit of the world change.
     * @param ID The ID of the world change.
     * @param player The player for whom the world change is handled.
     */
    static void handleWorldChange(String unit, String ID, Player player) {
        switch (unit) {
//            case "1":
//                switch (ID) {
//                    case "1.0":
//                        resetWorld();
//                        //Shipping and receiving
//
//                        break;
//                    case "3.0":
//                        resetWorld();
//                        //Shipping and receiving
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 131 65 -872 120 62 -882 117 70 -881");
//
//                        break;
//                    case "4.0":
//                        resetWorld();
//                        //Zion's Workshop
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 122 59 -827 127 62 -810 122 70 -827");
//                        // getChestsL4(player);
//                        break;
//                    case "5.0":
//                        resetWorld();
//                        //Shipping and receiving
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 134 52 -836 114 56 -821 111 71 -826");
//                        break;
//                    case "6.0":
//                        resetWorld();
//                        //Shipping and receiving
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 130 44 -833 111 48 -818 111 71 -826");
//                }
//                break;
            case "2":
                switch (ID) {
//                    case "2.0":
//                        resetWorld();
//                        //Shipping and receiving
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 117 48 -886 128 43 -877 119 70 -878");
//                        break;
                    case "3.0": //lesson 2
                        resetWorld();
                        //Santi's Factory
                        // Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 149 58 -829 156 60 -826 151 72 -835");
                        //Zions Workshop
                        //    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 109 60 -833 107 62 -830 125 71 -823");

                        spawnSchem("recycler.schem", new Location(player.getWorld(), 126, 72, -825));
                        spawnSchem("u2l2.schem", new Location(player.getWorld(), 160, 73, -836));


                        break;
//                    case "4.0":
//                        resetWorld();
//                        //Zions Workshop
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone 116 59 -825 114 61 -821 126 71 -823");
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock 149 72 -835 minecraft:dropper[facing=east]");
//                        //spawnSchem("u2l4.schem", new Location(player.getWorld(), 155, 72, -829));
//                        break;
//                    case "A.0":
//                        resetWorld();
//                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock 149 72 -835 minecraft:dropper[facing=east]");
//                        break;
                }
                break;
        }

        for(WorldChange worldChange : worldChanges) {
            if(worldChange.getUnit().equals(unit) && worldChange.getID().equals(ID)) {
                System.out.println("Running Command String");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone " + worldChange.getCommandString());
            }
        }
    }

    /**
     * Resets the game world to its initial state.
     */
    static void resetWorld() {
        //Shipping and receiving
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "fill 131 71 -869 111 74 -886 air");
        //Zion's Workshop
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "fill 131 71 -825 112 76 -810 air");
        //Santi's Factory
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "fill 148 72 -837 169 81 -814 air");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock 149 72 -839 minecraft:chest[facing=east]");

        //U1 Tutorial
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock -13 137 -1245 air");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock -16 139 -1246 air");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock -22 139 -1247 dirt");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clone -9 132 -1235 -9 132 -1235 -10 139 -1240");

        //U2 Tutorial
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "setblock -50 133 -1163 minecraft:blue_concrete");

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:item]");


    }

    /**
     * This method handles objective items based on the unit and ID provided.
     *
     * @param unit   The unit of the objective item.
     * @param ID     The ID of the objective item.
     * @param player The player for whom the objective items are handled.
     */
    static void handleObjectiveItems(String unit, String ID, Player player) {
        switch (unit) {
            case "1":
                switch (ID) {
                    case "1.2":
                        if (!player.getInventory().contains(Material.WRITTEN_BOOK)) {
                            //System.out.println("Making book");
                            ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
                            BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                            bookMeta.setTitle("Resource List");
                            bookMeta.setAuthor("Victoria");
                            bookMeta.addPage("Resource List:\n-20 Oak Planks\n-10 Sticks\n-4 Stone Axes");

                            bookItem.setItemMeta(bookMeta);
                            player.getInventory().addItem(bookItem);
                        }
                        break;
                    case "1.6.1":
                        ItemStack craft = new ItemStack(Material.CRAFTING_TABLE);
                        player.getInventory().addItem(craft);
                        break;
                    case "1.6.2":
                        craft = new ItemStack(Material.CRAFTING_TABLE);
                        player.getInventory().addItem(craft);
                        break;
                    case "1.13":
                        ItemStack sticks = new ItemStack(Material.STICK, 2);
                        ItemStack cobble = new ItemStack(Material.COBBLESTONE, 3);
                        player.getInventory().addItem(sticks);
                        player.getInventory().addItem(cobble);
                        break;
                    case "1.14.1":
                        ItemStack chests = new ItemStack(Material.CHEST, 3);
                        player.getInventory().addItem(chests);
                        break;

                    case "4.2.0":
                        chests = new ItemStack(Material.CHEST, 32);
                        player.getInventory().addItem(chests);
                        chests = new ItemStack(Material.ITEM_FRAME, 16);
                        player.getInventory().addItem(chests);
                        Commands.setStage(player, getPlayerPointer(player), "1", "4.2");
                        break;
                    case "4.5":
                        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                        bookMeta.setTitle("Order List");
                        bookMeta.setAuthor("Victoria");
                        bookMeta.addPage("Smelting Order:\n-20x Charcoal\n-4x Iron Ingots\n-5x Copper Ingot\n-10x Glass\n-11x Smooth Stone");
                        bookMeta.addPage("Tool Order:\n-6x Iron Axes\n-6x Iron Pixaxes\n-10x Torches\n-3x Shears");
                        bookMeta.addPage("Book Order:\n-7x Books\n-3x Bookshelves");

                        bookItem.setItemMeta(bookMeta);

                        ItemStack crafter = new ItemStack(Material.CRAFTING_TABLE);
                        player.getInventory().addItem(bookItem);
                        player.getInventory().addItem(crafter);
                        break;

                    case "5.6":
                        ItemStack EncoderBook = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta encoderMeta = (BookMeta) EncoderBook.getItemMeta();
                        encoderMeta.setTitle("Encoder Key");
                        encoderMeta.setAuthor("Victoria");
                        encoderMeta.addPage("RGB - Red, Green, Blue\n000, 001, 010, 111, ...\n\nHexadecimal -\n[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F]]\nA to F = 10 to 15");
                        EncoderBook.setItemMeta(encoderMeta);
                        player.getInventory().addItem(EncoderBook);

                        player.getInventory().addItem(new ItemStack(Material.OAK_SIGN, 45));
                        player.getInventory().addItem(new ItemStack(Material.ITEM_FRAME, 160));
                        player.getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK));
                        break;
                    case "5.13":
                        bookItem = new ItemStack(Material.WRITTEN_BOOK);
                        bookMeta = (BookMeta) bookItem.getItemMeta();
                        bookMeta.setTitle("Order List");
                        bookMeta.setAuthor("Victoria");
                        bookMeta.addPage("Order:\n0001 - 3\n001F - 2\n010C - 3\n1112 - 3\n1111 - 2");
                        //Black bed x3, Blue Wool x2, Green Stained Glass x3, white candle x3, white bed x2

                        bookItem.setItemMeta(bookMeta);
                        player.getInventory().addItem(bookItem);
                        break;
                    case "6.2":
                        System.out.println("Current System Labels");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give @p written_book{pages:['{\"text\":\"RGB - Red, Green, Blue\\n000, 001, 010, 111, ...\\n\\nHexadecimal -\\n[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F]\\nA to F = 10 to 15\\n\\n \"}'],title:\"Encoding Key\",author:Victoria} 1");
                        ///give @p written_book{pages:['{"text":"BB - Black Banner\\nBD - Black Dye\\nBB - Blue Bed\\nBW - Blue Wool\\nBC - Blue Candle\\nBC - Blue Concrete\\nGC - Green Candle\\nYB - Yellow Bed\\nYC - Yellow Carpet\\nWC - White Carpet\\nWC - White Concrete\\nMB - Magenta Bed\\nBT - Black Terracotta\\nWB - White Banner\\nWB - White Bed\\n\\n\\n "}'],title:"Storage List",author:Victoria}
                        break;
                    case "6.4":
                        System.out.println("New list of side by side");
                        //find the book in the players inventory and delete it
                        ItemStack[] items = player.getInventory().getContents();
                        for (ItemStack item : items) {
                            if (item != null && item.getType() == Material.WRITTEN_BOOK) {
                                player.getInventory().remove(item);
                            }
                        }
                        //give the new book
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "give @p written_book{pages:['{\"text\":\"BB - Black Banner\\nBD - Black Dye\\nBB - Blue Bed\\nBW - Blue Wool\\nBC - Blue Candle\\nBC - Blue Concrete\\nGC - Green Candle\\nYB - Yellow Bed\\nYC - Yellow Carpet\\nWC - White Carpet\\nWC - White Concrete\\nMB - Magenta Bed\\nBT - Black Terracotta\\nWB - White Banner\\nWB - White Bed\\n\\n\\n \"}'],title:\"Storage List\",author:\"Victoria\"}");
                        ///give @p written_book{pages:['{"text":"BB - Black Banner\\nBD - Black Dye\\nBW - Blue Wool\\nBC - Blue Candle\\nGC - Green Candle\\nYB - Yellow Bed\\nYC - Yellow Carpet\\nWC - White Carpet\\nMB - Magenta Bed\\nBT - Black Terracotta\\nWB - White Banner\\n\\n\\n\\n\\n\\n "}'],title:"Storage List",author:Victoria}
                        break;
                    case "6.6.A":
                        System.out.println("Give Puzzle A");
                        break;
                    case "6.6.B":
                        System.out.println("Give Puzzle B");
                        break;
                    case "6.10":
                        System.out.println("Give 100 List");
                        break;
                }
                break;
            case "2":
                switch (ID) {
                    case "2.2":
                        ItemStack OakSigns = new ItemStack(Material.OAK_SIGN, 6);
                        ItemStack OakPlanks = new ItemStack(Material.OAK_PLANKS, 1);
                        ItemStack Sticks = new ItemStack(Material.STICK, 1);
                        ItemStack Cobbles = new ItemStack(Material.COBBLESTONE, 1);
                        ItemStack Coal = new ItemStack(Material.COAL, 1);

                        player.getInventory().addItem(OakSigns);
                        player.getInventory().addItem(OakPlanks);
                        player.getInventory().addItem(Sticks);
                        player.getInventory().addItem(Cobbles);
                        player.getInventory().addItem(Coal);
                        break;

                    case "3.1.1":
                        if (!player.getInventory().containsAtLeast(new ItemStack(Material.BEDROCK), 1)) {
                            //Chest, oak planks, sticks, barrier block named "Recycler_1"
                            OakPlanks = new ItemStack(Material.OAK_PLANKS, 6);
                            ItemStack Bedrock = new ItemStack(Material.BEDROCK, 1);

                            player.getInventory().addItem(OakPlanks);
                            player.getInventory().addItem(Bedrock);
                        }
                        break;
                    case "3.2.1":
                        ItemStack oaklogs = new ItemStack(Material.OAK_LOG, 5);
                        ItemStack oakplanks = new ItemStack(Material.OAK_PLANKS, 5);
                        player.getInventory().addItem(oaklogs);
                        player.getInventory().addItem(oakplanks);
                        break;
                    case "4.5":
                        ItemStack hopper = new ItemStack(Material.HOPPER, 10);
                        ItemStack itemFrame = new ItemStack(Material.ITEM_FRAME, 5);
                        ItemStack redstone = new ItemStack(Material.REDSTONE, 5);
                        ItemStack dispenser = new ItemStack(Material.DISPENSER, 5);
                        ItemStack chest = new ItemStack(Material.CHEST, 10);


                        ItemStack dandelion = new ItemStack(Material.DANDELION, 10);
                        ItemStack poppy = new ItemStack(Material.POPPY, 10);
                        ItemStack cornflower = new ItemStack(Material.CORNFLOWER, 10);
                        ItemStack tulip = new ItemStack(Material.ORANGE_TULIP, 10);


                        player.getInventory().addItem(hopper);
                        player.getInventory().addItem(itemFrame);
                        player.getInventory().addItem(redstone);
                        player.getInventory().addItem(dispenser);
                        player.getInventory().addItem(chest);

                        player.getInventory().addItem(dandelion);
                        player.getInventory().addItem(poppy);
                        player.getInventory().addItem(cornflower);
                        player.getInventory().addItem(tulip);

                        break;
                    case "A.1":
                        ItemStack log = new ItemStack(Material.OAK_LOG, 64);
                        ItemStack redstone1 = new ItemStack(Material.REDSTONE, 5);
                        ItemStack hoppers = new ItemStack(Material.HOPPER, 20);
                        ItemStack chests = new ItemStack(Material.CHEST, 20);
                        ItemStack itemFrames = new ItemStack(Material.ITEM_FRAME, 10);
                        ItemStack dispensers = new ItemStack(Material.DISPENSER, 10);

                        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
                        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
                        bookMeta.setTitle("Order");
                        bookMeta.setAuthor("Zion");
                        bookMeta.addPage("Order:\n\n-64 Oak Planks\n-64 Sticks\n-64 Oak Fences\n-64 Oak fence gates");

                        bookItem.setItemMeta(bookMeta);
                        player.getInventory().addItem(bookItem);
                        player.getInventory().addItem(log);
                        player.getInventory().addItem(redstone1);
                        player.getInventory().addItem(hoppers);
                        player.getInventory().addItem(chests);
                        player.getInventory().addItem(itemFrames);
                        player.getInventory().addItem(dispensers);

                        break;
                }
                break;
        }
    }

    /**
     * Handles actor positions based on the unit and ID provided.
     *
     * @param unit The unit identifier.
     * @param ID   The ID of the actor.
     */
    static void handleActorPositions(String unit, String ID) {
        switch (unit) {
            case "1":
                switch (ID) {
                    case "1.0":
                        resetActorPositions(); //This function is only run for the first ID of the lesson
                        // Serenity     [156, -908]
                        // Victoria     [128, -844]
                        // Zion         [131,-822]
                        HuMInLabPlugin.npcManager.setNPCPos("Serenity", 155.5, 71, -907.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 127.5, 71, -843.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 130.5, 71, -821.5);

                        break;
                    case "1.18":
                        // Mayor Goodway  [128, -844]
                        HuMInLabPlugin.npcManager.setNPCPos("Mayor Goodway", 134.5, 71, -861.5);
                        break;
                    case "3.0":
                        resetActorPositions();
                        // Serenity     [156, -908]
                        // Zion         [128, -844]
                        HuMInLabPlugin.npcManager.setNPCPos("Serenity", 155.5, 71, -907.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 127.5, 71, -843.5);
                        break;
                    case "4.0":
                        resetActorPositions();
                        // Serenity     [156, -908]
                        // Zion         [131,-822]
                        HuMInLabPlugin.npcManager.setNPCPos("Serenity", 155.5, 71, -907.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 130.5, 71, -821.5);
                        break;
                    case "5.0":
                        resetActorPositions();
                        // Serenity     [156, -908]
                        // Victoria     [128, -844]
                        HuMInLabPlugin.npcManager.setNPCPos("Serenity", 155.5, 71, -907.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 127.5, 71, -843.5);
                        break;

                    case "5.2":
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 131.5, 71, -825.5);
                }
                break;
            case "2":
                switch (ID) {
                    case "2.0":
                        resetActorPositions();
                        //Victoria
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 130.5, 71, -869.5);
                        break;
                    case "A.0":
                    case "3.0":
                        resetActorPositions();
                        // Santiago     [150,-875]
                        // Zion         [131,-822]
                        HuMInLabPlugin.npcManager.setNPCPos("Santiago", 149.5, 72, -840.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 130.5, 71, -821.5);
                        break;
                    case "4.0":
                        resetActorPositions();
                        // Victoria     [128,-844]
                        // Zion         [131,-822]
                        // Santiago     [150,-875]
                        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 127.5, 71, -843.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Zion", 130.5, 71, -821.5);
                        HuMInLabPlugin.npcManager.setNPCPos("Santiago", 149.5, 72, -840.5);
                        break;
                }
                break;
        }
    }

    /**
     * Resets the positions of all actors to their default positions.
     */
    static void resetActorPositions() {
        HuMInLabPlugin.npcManager.setNPCPos("Serenity", 155.5, 71, -907.5);
        HuMInLabPlugin.npcManager.setNPCPos("Victoria", 127.5, 0, -843.5);
        HuMInLabPlugin.npcManager.setNPCPos("Zion", 130.5, 0, -821.5);
        HuMInLabPlugin.npcManager.setNPCPos("Mayor Goodway", 134.5, 0, -861.5);
        HuMInLabPlugin.npcManager.setNPCPos("Santiago", 149.5, 0, -874.5);
    }

    /**
     * Spawns a schematic at the specified location.
     *
     * @param schem    The name of the schematic file.
     * @param location The location where the schematic should be spawned.
     */
    private static void spawnSchem(String schem, Location location) {
        try {
            //Load Scheme
            File file = null;
            for (File f : schematicFiles) {
                if (f.getName().equals(schem)) {
                    file = f;
                }
            }
            if (file == null) {
                System.out.println("File " + schem + " not found");
                return;
            }

            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();

            //Paste Scheme
            World adaptedWorld = BukkitAdapter.adapt(location.getWorld());
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(true)
                    .build();
            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                System.out.println("WorldEditException: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
