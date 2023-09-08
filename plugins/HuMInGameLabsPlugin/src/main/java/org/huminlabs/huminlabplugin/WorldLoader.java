package org.huminlabs.huminlabplugin;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The WorldLoader class is responsible for loading and managing worlds in the HuMInLabPlugin.
 *
 * This is an experimental system that is not currently in use. The idea is that we have one server and we load a new world per player whenever they join.
 * We can then unload the world when they leave. This would allow us to have a theoretically infinite number of players on one server at once.
 */


public class WorldLoader {
    static HuMInLabPlugin plugin;
    private static ArrayList<PlayerPointer> pointers = new ArrayList<>();

    static int slots = 10;
    public static World[] worlds = new World[slots];
    public static String[] playerClaims = new String[slots];

    /**
     * Constructor for the WorldLoader class.
     *
     * @param plugin The HuMInLabPlugin instance.
     */
    public WorldLoader(HuMInLabPlugin plugin) {
        this.plugin = plugin;
        this.worlds = new World[10];

        //loadWorlds();
    }

    /**
     * Loads the worlds for the HuMInLabPlugin.
     */
    public static void loadWorlds() {


        for (int i = 0; i < 3; i++) {
            // worlds[i] = getWorld("world" + i);
            worlds[i] = getWorld("world" + i);
            playerClaims[i] = null;
        }
        System.out.println("Worlds loaded");
    }

    /**
     * Retrieves a world by name. If the world does not exist, it creates a new one.
     *
     * @param name The name of the world.
     * @return The retrieved or created world.
     */
    private static World getWorld(final String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            return world;
        }

        File worldDir = new File("/static/worlds");
        try {
            FileUtils.copyDirectory(worldDir, new File("/static/worlds/" + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WorldCreator creator = new WorldCreator(name);
        world = Bukkit.createWorld(creator);

        return world;
    }

    /**
     * Claims a world for a player.
     *
     * @param player The player claiming the world.
     */
    private static void claimWorld(Player player) {
        for (int i = 0; i < slots; i++) {
            if (playerClaims[i] == null) {
                playerClaims[i] = player.getUniqueId().toString();
                teleportPlayer(player, i);
                System.out.println("World " + i + " claimed by " + player.getName());
                return;
            }
        }
        System.out.println("WARNING: No more worlds available");
    }

    /**
     * Finds a world for a player. If the player already has a claimed world, they are teleported to it.
     * Otherwise, a new world is claimed for the player.
     *
     * @param player The player to find a world for.
     */
    public static void findWorld(Player player) {
        for (int i = 0; i < slots; i++) {
            if (playerClaims[i] != null) {
                if (playerClaims[i].equals(player.getUniqueId().toString())) {
                    teleportPlayer(player, i);
                    System.out.println(player.getName() + " returning to world " + i);
                    return;
                }
            }
        }
        claimWorld(player);
    }

    /**
     * Teleports a player to a specific world.
     *
     * @param player   The player to teleport.
     * @param worldID  The ID of the world to teleport the player to.
     */
    private static void teleportPlayer(Player player, int worldID) {
        Location location = new Location(worlds[worldID], 155.5, 71, -907.5);
        player.teleport(location);
    }


}
