package org.huminlabs.huminlabplugin.Objective;


public class WorldChange {
    private String name;
    private String unit;
    private String id;
    private String begin;    //Starting x y z coordinate for the source region to clone (ie: first corner block)
    private String end;      //Ending x y z coordinate for the source region to clone (ie: opposite corner block)
    private String dest;     //Use the lowest x,y,z values for the destination. This will be the bottom NorthWest corner of the destination region

    //Note that this is a string, not an int[], that is because it will be appended to a /clone command

    //Setters
    public WorldChange(String name, String unit, String id, String begin, String end, String dest) {
        this.name = name;
        this.unit = unit;
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.dest = dest;
    }

    //Getters
    public String getID() {
        return id;
    }
    public String getUnit() {
        return unit;
    }
    public String getBegin() {
        return begin;
    }
    public String getEnd() {
        return end;
    }
    public String getDest() {
        return dest;
    }

    /**
     * Returns the command string for the world change operation.
     *
     * @return The command string for the world change operation.
     */
    public String getCommandString() {
        return "/clone " + begin + " " + end + " " + dest;
    }
}
