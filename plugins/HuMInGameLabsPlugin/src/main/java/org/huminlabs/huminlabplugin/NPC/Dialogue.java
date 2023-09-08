package org.huminlabs.huminlabplugin.NPC;



/**
 * The Dialogue class represents a dialogue in a conversation.
 * It contains information about the dialogue's ID, unit, dialog, response, trigger, and actor.
 */
public class Dialogue {
    public String id; // The ID of the dialogue
    public String unit; // The unit associated with the dialogue
    public String[] dialog; // An array of dialog lines
    public String[] response; // An array of response options
    public String[] trigger; // An array of trigger conditions
    public String actor; // The actor associated with the dialogue

    /**
     * Constructs a Dialogue object with the specified parameters.
     *
     * @param ID The ID of the dialogue.
     * @param dialog An array of dialog lines.
     * @param response An array of response options.
     * @param trigger An array of trigger conditions.
     * @param Actor The actor associated with the dialogue.
     */
    public Dialogue(String ID, String[] dialog, String[] response, String[] trigger, String Actor) {
        this.id = ID;
        this.dialog = dialog;
        this.response = response;
        this.trigger = trigger;
        this.actor = Actor;
    }

    /**
     * Returns the ID of the dialogue.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the unit associated with the dialogue.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Returns an array of dialog lines.
     */
    public String[] getDialog() {
        return dialog;
    }

    /**
     * Returns an array of response options.
     */
    public String[] getResponse() {
        return response;
    }

    /**
     * Returns an array of trigger conditions.
     */
    public String[] getTrigger() {
        return trigger;
    }

    /**
     * Returns the actor associated with the dialogue.
     */
    public String getActor() {
        return actor;
    }

    /**
     * Sets the unit associated with the dialogue.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
