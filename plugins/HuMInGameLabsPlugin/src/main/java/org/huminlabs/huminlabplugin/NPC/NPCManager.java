package org.huminlabs.huminlabplugin.NPC;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;
import org.huminlabs.huminlabplugin.NPC.NPC;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NPCManager {
    private HuMInLabPlugin plugin;
    private DialogueManager dialogueManager;
    private ArrayList<NPC> npcs;

    public NPCManager(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    //Case List
    //  Serenity
    //  Victoria
    //  Zion
    //  Mayor Goodway
    //  Santiago

    /**
     * This method is used to load NPCs (Non-Player Characters) for a player in the game.
     * It creates NPC objects with specific details such as name, texture, signature, and position,
     * and adds them to an ArrayList of NPCs.
     *
     * @param player The player for whom the NPCs are being loaded.
     */
    public void loadNPCs(Player player) {
        // Get the dialogue manager instance
        dialogueManager = HuMInLabPlugin.dialogueManager;

        // Create a new ArrayList to store the NPCs
        npcs = new ArrayList<NPC>();

        //Serenity
        String signature = "yMUQXd/k4q1vgDKVgWTAsaGvn42gce9gETko6RSQ9vxiRVq5MemCQuDS9w0rS/6ejoFXPDtbdu4l5lFzFEnqCS9SRu1G68Fbjp64WukrzskA+/rc/rutpVt3QlLqMzJGabsMj8yoliu2MxTAT8csSuLrKoHyuDQOHxRFq9qb/C8TiGUDMzJNdxwG+QHceoi2sGlHI5GUWzW3JVmUOCQX/HcbQ60Qwt5jCE9OeWgDlJ6gWBSuUPT/1VnbNlqe1ugVxiCiZltn20B15EHqB/If4/upxPEiKB+TAwnH08D631cNBIId/cc1ytFh6UdLG47/i+H5JqIrCPeu84era08Edr85Iz3hI3vgpdk0Z4x2ehz0mhMPIYL9oEhMk20slgqrNhOgVCIsRUy4wAV1Q4ODCEDBfjYOVzyWCufR8Qexw4So1OpoK3R2k2x7xSIvslphq9ebG99C+4UjlSm1xes5Oc1VwEA+HH7x6pOy2jOLbhHzTZOD0UgjEqMWW8G1TNKBX/nbaYrYtOOuHdquJ+RsILfMVUNsHC+XOd1Tvj8Nw04pf1lYQCgLDnas+3/3KdZMAyOVvTtyAgIqPsM+KaPJKfuBo/tfwWf3C8gZx2tOcv4DLnVgtUQJZkLSBjBt6whXlGo5N99ViyZHCKt3lhEMX1hVXb/ac/TTG9n7L7VHGxQ=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3ODYzODc1NjM0OSwKICAicHJvZmlsZUlkIiA6ICIyMWUzNjdkNzI1Y2Y0ZTNiYjI2OTJjNGEzMDBhNGRlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZXlzZXJNQyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lY2MwZGE4ZTY4NDUyOTk0YjBjMjQwYjcyZmMxN2Y4M2UwNDBiNDMyYTY4NDNiNjIxOTRjYmI5NWJkMWFkNDRmIgogICAgfQogIH0KfQ==";

        makeNPC(texture, signature, "Serenity", player, 155.5, 71, -907.5);

        //Victoria
        signature = "Eq4+sXhgFY9iICwFYzPF5sF6fMJJGM4Wp3ECGYNJ6e5O1jhSMGJcQnpRyF7a770pNniz56gTlH6C6AT+aYIEUVjscBBAaAWIxNxR17mUmp0Ggy/6aMKzv8SPO9e5s7G6uMznOYdxEUiSUvSQ4qfM5mC1BAHcv5CnP68i8rGGFcGgEz7rkcHyiQpUkxJzJ1ACbbLmr8BfIINIBYgQVUXYLzZuT/XlutyGDkEy+DSB8IzgsLFlS0jSzVTPJZ3Ev45REsswUugZ8+LLampxHp+L1yehDetlAXM0h20m2mjxV+RpFC0XuPwi6xlhadSjBRCnr9o0Yf2NFBz1sXbAWr7d9xeVABw7X2FzcDCdcmWCO1aL60oagzzuxOYmnKxY9WnlZFpbLyJdq1UbQq+8ec9YsP+EPPrqPPA4L3pPlW23pOR3IFA2jU7/hZoTlPnPZoaMJhV+P+eMqhxSVFANE1Ls02Wzv3/vgv9zJQoCSEJht7i8XzmQ8j7iXrMpftCRl+S+09CCOG8vs/6T2ePmvRk458zz1Oi9XvXQmWl6gMfCnT8btWXdOD7T4f2npxzwVFsUBMKOEBFQGSCCx5lu8fcdGFM0gX1yz9zvoR3SAYcizvk+d6uH8W5EU6GPpYrz5RQ4a5tOilpxRBJkABfxF7lFmTHfRBojuc0QVBr69lmJrsk=";
        texture = "ewogICJ0aW1lc3RhbXAiIDogMTU4OTE5MDgwODI5MiwKICAicHJvZmlsZUlkIiA6ICI3NzI3ZDM1NjY5Zjk0MTUxODAyM2Q2MmM2ODE3NTkxOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJsaWJyYXJ5ZnJlYWsiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNmNGRjM2U2NWI2NzkyNjAzM2IwYWY4M2E4ZGFlNmM2MzhkMzc3MDc3ODlmM2E2MWQ3OGI2ZmRiZGY5MjliZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";

        makeNPC(texture, signature, "Victoria", player, 127.5, 71, -843.5);

        //Zion
        signature = "Ulz70gd1QAdj/WvbD6yxsZRJS7hn5CNxz7eU6kS9oAiahbcS/FHpqtsvvsIc4ttYLlvVeouAX37IsTgAOlFHeeeRYq/3Aq8HMhLcN8750yIQpZLlAm1n62YIxq/oZ23AYrRsLvXzsSJH6YnJ8IrkjedBYCOV9gTHlvKCAUL/her417sxBC1TZZ2HP0QV4BBtCzuB7FE8EjXqVJ135YP1JDibuathAPBOAfrUA9nTw1ve9oKsn2RaFNGjpEY2B23GhAnqrG3f5UqlPv9XEmSTEij2qALvOTwwNggKk6zjE3rtxNB+qPiu7jrC+4RJEKoeQfpoB1ChyIhXZ7FFqYHM9wg+nbri+G6ZdUehoLNZp6VC7EZy5fQ5nhcFDn1NJrhg/OW6eAmM77O6yLhwBezLQqTZgnMD+XuEB2X3tleHVO6cye7mH0Bu5ES6uCeg/wNDSyBMlomzOitgh1M6zl+eJrGdyolx2pDpVIpLACuAhPV0r7n2tkvUyP57Kw6m6p92AuHlJErazCPMacpFlZURAJ27DJu6ZZ0DGERCdolwNQcdCynFEdOPI7dAa87I1PTUeS9mGWQNbM4sakyCpKIlh7UseDq9uo0flBii6FJGgdkoF8L2fD923K5JByltG+WEIuQ98kUJ2WbT2R2xvgqPM7N93AeSzyeNZI8Jrn5oCuo=";
        texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3OTI1NjUzMjAyNywKICAicHJvZmlsZUlkIiA6ICIwYzE1OTI3Yjc4OTY0MTA3OTA5MWQyMjkxN2U0NmIyYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJZb3VBcmVTY2FyeSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NDc4MWUxZTE5NzQxYWRiNjQ4ZmU4ZjNiMTNlOWZmYjBhYzJlNWE1MTc1NGNhMDVkYWJhOTFmY2Y0N2NkMDI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";

        makeNPC(texture, signature, "Zion", player, 130.5, 71, -821.5);

        //Mayor Goodway
        signature = "NKCe24NCAHtiRllBtCytMl9iirZ6FYx9ak/YB0jx+N6LwQ9yX7etVRYV86+VUAx2gG6+4P3aEDV4/GY+NWYWEWy6qKtd6E4xRTxZMpCr28HmcZizXHS+rGiW6wLqZhXobn4+v+m9uTpyJwAkLcuQfJFEe2cMY7PADOf9oWnWhcsVAuN5xvbKKqpNQt+k9NIbxreoEkpE1GOt1fMnVS38sltjuNHF4JfYpmgeCcyKB3jzGiGPJ4tHP9B8ReygRN3wb91caDD72gj+SZVlcR3/TYiB5Ejxt5xGc5YsgOQTzEpUEvAmu8AXUwW1CW6kGpj1pXvNqzBKL7shUQ2XBTHsx3CK/FCOj1a6d3nDTRSFfP/BYmQsLYRDv4j280aOp66x5pC6WXHxXL3xc+yUCFZ/s1L+DWlnXJPe0UpS7ZHK/PNJpsfXSodjiFAbu5nCyzslu8np/XVRscCqHbT6JvctENyMeSzj/AdtIqn2yfRF8zvKoKXiQ/WdiW7avRvzAEuwDzD1ZQ/WLQhgTQVGbcwHi9x7hSYa18R25FsB7n9wHNUDhnkptvOWyn3gP3UyhlHzu7D/v267dmuuBP+eHRvygQiHASetYNYmMJOzaC2Sm7f1UsuYx+4oQ2Wx2xICz8ygS88XosyfZlU526VNYEJ5T+Z0/QVjZkeUD3cUJC7QhXw=";
        texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3MjQyNzQzNzE4MywKICAicHJvZmlsZUlkIiA6ICJhYzIzZmEzNzRhMmM0YjU2YmYzYzU2ZTVhODY3NjIzNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJxdWVlbnNoYW5pd2EiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMxYzljNDA4MTM3MzlmNzBkM2E0ZjQ1NTA1NDg5MzU5YjU2Zjc3ODhkZWVjYTk0NGFiYjA1ZDViYTEwYzM1IgogICAgfQogIH0KfQ==";

        makeNPC(texture, signature, "Mayor Goodway", player, 131.5, 0, -821.5);

        //Santiago
        signature = "MmJ/Y1jar10BiZtz/IVkB4yAe5hRvzi6lPRTEPhuJ2xEPDuVZfQgSfokD3UKBN2VMsa4GS81y3z1NGeDgntHf1TuXGVHrzzkANwzLn6mBv6zbPAy9Bc4Tehq/YWSeLerJCjo4g4eJxqcEvSBe65JRqhVWPnISOBMHRTZROgb9YtUcBcp87+vpSPTEFsMtnZ0p+3nm9Mf2sZJzSjMunVMjZoXowCCC5X0lVLh1DyyLxSeTM+MWS7LwGXKEvWf0eimYF1xuQ45JVe7AmFM6UXanhzZwvlOhJb0vskxps3AUxm5u1NW/yYm9ekVz61KvzJihGM+lgwL4knEzvcIypQtug7AnkT52AKZqtVvxLUFyVPIbOygZqhIs4vrdagr+V+KzWNL0p0cnZltXe9a7bDCX2ruirHJ8vBpWFNCYmCjHn9wYPtwnGEfrW0HHMZJSeDw/jIwDZlIaLTcpyWwR/7fy7i/t14CWNDbEAJFs1VyuYBeAofgTzOKhnxA/kEVSgVHyaxBq/hF/j7fa2eXcTmj6W2a65wVyi8c0KsufufWqkI5AGWKDNQrIqjA/Z0rQ2JrfdCRN7H6shVBArOI+RCU/fZc82vzRCo3P+AcyggdVkYUAPUX1bkSsonJac8SFx5ZCyQbVz372cA5iFc+IM6yK225g5CC8Njfv2zrsOe3xEU=";
        texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3OTkyODI2NzEzOSwKICAicHJvZmlsZUlkIiA6ICI2NmI0ZDRlMTFlNmE0YjhjYTFkN2Q5YzliZTBhNjQ5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmFzdG9vWXNmIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3Y2U5NDU5YjA2Yzk2YmI5NzFjNWJiOGVhZmIzODVjNmEwMjZkMDNjYzczODYzOWQxNmEyZjI0Y2YyYzE3YjYiCiAgICB9CiAgfQp9";

        makeNPC(texture, signature, "Santiago", player, 149.5, 71, -841.5);

        System.out.println("NPCs loaded: " + npcs.size());
    }

    /**
     * Creates a new NPC with the specified texture, signature, name, and player.
     * Sets the dialogue for the NPC and spawns it at the specified coordinates.
     * Adds the NPC to the list of NPCs.
     *
     * @param texture   The texture of the NPC.
     * @param signature The signature of the NPC.
     * @param name      The name of the NPC.
     * @param player    The player associated with the NPC.
     * @param x         The x-coordinate of the NPC's spawn location.
     * @param y         The y-coordinate of the NPC's spawn location.
     * @param z         The z-coordinate of the NPC's spawn location.
     */
    private void makeNPC(String texture, String signature, String name, Player player, double x, double y, double z) {
        NPC npc = new NPC(name, signature, texture, plugin, player);
        npc.setDialogue(dialogueManager.getDialogue(name));
        npc.spawnNPC(player, x, y, z);
        npcs.add(npc);
    }

    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public void setNPCPos(String name, double x, double y, double z) {
        boolean found = false;
        for(NPC npc : npcs) {
            if(npc.name.equals(name)) {
                found = true;
                npc.setPos(x, y, z);
                npc.sendPackets();
            }
        }
        if(!found) {
            System.out.println("NPC [" + name + "] not found");
        }
    }
}
