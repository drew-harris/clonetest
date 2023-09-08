package org.huminlabs.huminlabplugin.Objective;

/**
 * The Objective class represents an objective in a quest.
 * It contains information about the objective's ID, unit, objective, and location.
 */

public class Objective {
    private String id;
    private String unit;
    private String objective;
    private int[] location;

    //Setters
    public Objective(String id, String objective, String unit, int[] location) {
        this.id = id;
        this.objective = objective;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    //Getters
    public String getId() {
        return id;
    }
    public String getObjective() {
        return objective;
    }
    public int[] getLocation() {
        return location;
    }
    public String getUnit() {
        return unit;
    }

    //Operators
    public boolean equals(Objective objective) {
        return (this.id.equals(objective.getId()) && this.unit.equals(objective.getUnit()));
    }
}
