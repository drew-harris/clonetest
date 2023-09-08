package org.huminlabs.huminlabplugin.NPC;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.BookItem;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

/**
 * The NPC class represents a non-player character in a game.
 * It contains methods for creating and interacting with NPCs.
 */
public class NPC {
    HuMInLabPlugin plugin;
    Player player;
    GameProfile gameProfile;
    public String name;
    public ServerPlayer npc;

    public Dialogue[] dialogue;

    /**
     * Constructs an NPC object with the specified name, signature, texture, plugin, and player.
     *
     * @param name      The name of the NPC.
     * @param signature The signature for the NPC's texture.
     * @param texture   The texture for the NPC.
     * @param plugin    The plugin instance.
     * @param player    The player interacting with the NPC.
     */
    public NPC(String name, String signature, String texture, HuMInLabPlugin plugin, Player player) {
        this.name = name;
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);
        this.gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
        this.plugin = plugin;
        this.player = player;

        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel world = serverPlayer.getLevel();

        npc = new ServerPlayer(server, world, gameProfile);
    }

    /**
     * Sends the necessary packets to display the NPC to the player. Not displaying dialogue but the Player Entity itself.
     * This method should be called after the NPC is created.
     */
    public void sendPackets() {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        connection.send(new ClientboundAddPlayerPacket(npc));
    }

    /**
     * Spawns the NPC at the specified coordinates.
     *
     * @param player The player to spawn the NPC for.
     * @param x      The x-coordinate of the spawn location.
     * @param y      The y-coordinate of the spawn location.
     * @param z      The z-coordinate of the spawn location.
     */
    public void spawnNPC(Player player, double x, double y, double z) {
        setPos(x, y, z);
        sendPackets();
    }

    /**
     * Sets the position of the NPC.
     *
     * @param x The x-coordinate of the new position.
     * @param y The y-coordinate of the new position.
     * @param z The z-coordinate of the new position.
     */
    public void setPos(double x, double y, double z) {
        npc.setPos(x, y, z);
    }

    /**
     * Sets the dialogue for the NPC.
     *
     * @param dialogue The dialogue array to set.
     */
    public void setDialogue(Dialogue[] dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * Gets the ID of the NPC.
     *
     * @return The ID of the NPC.
     */
    public int getID() {
        return npc.getId();
    }

    /**
     * Runs the dialogue for the NPC.
     *
     * @param player The player interacting with the NPC.
     * @param unit   The unit of the dialogue.
     * @param id     The ID of the dialogue.
     */
    public void runDialogue(Player player, String unit, String id) {
        if (id == null) {
            id = "0.0";
        }

        for (int i = 0; i < dialogue.length; i++) {
            if (dialogue[i].getId().equals(id) && dialogue[i].getUnit().equals(unit)) {

                if (i < dialogue.length) {
                    String[] speech = dialogue[i].getDialog();
                    String[] response = dialogue[i].getResponse();

                    Component bookTitle = Component.text("");
                    Component bookAuthor = Component.text("");

                    ArrayList<Component> pages = new ArrayList<>();
                    for (int j = 0; j < speech.length - 1; j++) {
                        String s = speech[j];
                        pages.add(Component.text(s));
                    }

                    TextComponent lastPage = Component.text(speech[speech.length - 1]);
                    for (int j = 0; j < response.length; j++) {
                        lastPage = lastPage.append(
                                Component.text("\n" + response[j])
                                        .color(NamedTextColor.DARK_GREEN)
                                        .decorate(TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.runCommand("/setstage " + unit + " " + dialogue[i].trigger[j]))
                        );
                    }

                    pages.add(lastPage);
                    Collection<Component> bookPages = pages;
                    Book book = Book.book(bookTitle, bookAuthor, bookPages);
                    plugin.adventure().player(player).openBook(book);
                }
                break;
            }
        }
    }
}
