package org.huminlabs.huminlabplugin.Objective;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

/**
 * The PlayerPointer class represents a player's pointer in the HuMInLabPlugin.
 * It stores information about the player's UUID, objective ID, and unit.
 */
public class PlayerPointer {
    private String uuid;
    private String objectiveID;
    private String unit;

    /**
     * Constructs a PlayerPointer object with the given UUID.
     * The objective ID and unit are set to default values of "0.0" and "0" respectively.
     *
     * @param uuid The UUID of the player.
     */
    public PlayerPointer(String uuid) {
        this.uuid = uuid;
        this.objectiveID = "0.0";
        this.unit = "0";
    }

    /**
     * Constructs a PlayerPointer object with the given UUID, unit, and objective ID.
     *
     * @param uuid        The UUID of the player.
     * @param unit        The unit associated with the player.
     * @param objectiveID The objective ID associated with the player.
     */
    public PlayerPointer(String uuid, String unit, String objectiveID) {
        this.uuid = uuid;
        this.objectiveID = objectiveID;
        this.unit = unit;
    }
    //Getters
    public String getUUID() {
        return uuid;
    }
    public String getObjectiveID() {
        return objectiveID;
    }
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit and objective ID for the player.
     * It also updates the backend with the new unit and objective ID.
     * If the objective ID is "0.0", the unit is set to "0".
     *
     * @param unit        The new unit to be set.
     * @param objectiveID The new objective ID to be set.
     */
    public void setObjective(String unit, String objectiveID) {
        this.unit = unit;
        this.objectiveID = objectiveID;
        HuMInLabPlugin.backendRequestHandler.objectiveUpdate(uuid, unit, objectiveID);

        if(objectiveID.equals("0.0")){
            this.unit = "0";
        }
    }
}