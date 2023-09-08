/**
 * The HuMInLabPlugin class represents the main plugin class for the HuMInLab plugin.
 * It initializes and manages various components of the plugin.
 */
package org.huminlabs.huminlabplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.humingamelab.mc.api.Api;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.A;
import org.huminlabs.huminlabplugin.Backend.BackendRequestHandler;
import org.huminlabs.huminlabplugin.NPC.DialogueManager;
import org.huminlabs.huminlabplugin.NPC.NPC;
import org.huminlabs.huminlabplugin.NPC.NPCManager;
import org.huminlabs.huminlabplugin.Objective.ObjectiveStorage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public final class HuMInLabPlugin extends JavaPlugin {
    public static HuMInLabPlugin plugin;
    private BukkitAudiences adventure;
    public static EventListeners eventListeners;
    public static NPCManager npcManager;
    public static DialogueManager dialogueManager;
    public static ObjectiveStorage objectiveStorage;
    public static BackendRequestHandler backendRequestHandler;
    public static WorldLoader worldLoader;

    public Api api = new Api();

    /**
     * Returns the instance of the HuMInLabPlugin.
     *
     * @return The HuMInLabPlugin instance.
     */
    public static HuMInLabPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Set up adventure and event listeners
        this.adventure = BukkitAudiences.create(this);
        eventListeners = new EventListeners(this);
        getServer().getPluginManager().registerEvents(eventListeners, this);

        // Set up npc manager with interaction listener
        npcManager = new NPCManager(this);
        npcInteractionListener();

        // Set up dialogue manager
        try {
            dialogueManager = new DialogueManager();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Load Objectives and player pointers
        objectiveStorage = new ObjectiveStorage(this);

        // Command registration
        this.getCommand("getstage").setExecutor(new Commands(plugin));
        this.getCommand("setstage").setExecutor(new Commands(plugin));
        this.getCommand("nextstage").setExecutor(new Commands(plugin));
        this.getCommand("prevstage").setExecutor(new Commands(plugin));

        // Backend setup
        String URL = "https://fs73ztf2angzrl5wenenojtq4u.appsync-api.us-east-1.amazonaws.com/graphql";
        String auth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1bWluZ2FtZWxhYi5jb20iLCJzdWIiOiJ0ZXN0X2FkbWluIiwiZW1haWwiOiJ0ZXN0X2FkbWluIiwidWJfYWNjZXNzTGV2ZWwiOiJBTExPV0VEX0ZPUl9BTEwiLCJ1Yl9ncm91cHMiOiJbXSIsImlhdCI6MTY2NjIzMjU2NywiZXhwIjoxNjY2MzE4OTY3fQ.1ylUsmcDnmMAMw6ydRC5nMZSuviOJDYCc7CbU9FFsGY";
        backendRequestHandler = new BackendRequestHandler(this, URL, auth);

        // World setup
        worldLoader = new WorldLoader(this);

        System.out.println("HuMInLabsPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    /**
     * Sets up the NPC interaction listener using ProtocolLib.
     */
    public void npcInteractionListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer packet = event.getPacket();

                        int entityID = packet.getIntegers().read(0);
                        ArrayList<NPC> npcs = npcManager.getNPCs();
                        for (int i = 0; i < npcs.size(); i++) {
                            if (npcs.get(i).getID() == entityID) {

                                Player player = event.getPlayer();
                                String id = ObjectiveStorage.getPlayerPointer(player).getObjectiveID();
                                String unit = ObjectiveStorage.getPlayerPointer(player).getUnit();
                                System.out.println("Click: " + unit + " " + id);
                                if (!id.equals("0.0")) {
                                    npcs.get(i).runDialogue(player, unit, id);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Returns the BukkitAudiences instance used for sending messages to players.
     *
     * @return The BukkitAudiences instance.
     * @throws IllegalStateException if the plugin is disabled.
     */
    public @NonNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}