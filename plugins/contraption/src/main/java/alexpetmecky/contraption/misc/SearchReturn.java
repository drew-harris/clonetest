package alexpetmecky.contraption.misc;

import java.util.ArrayList;

public class SearchReturn {
    private NodeMeta finalNode;
    private ArrayList<NodeMeta> visited;

    public SearchReturn(NodeMeta finalNode, ArrayList<NodeMeta> visited){
        this.finalNode = finalNode;
        this.visited = visited;
    }

    public NodeMeta getFinalNode() {
        return finalNode;
    }

    public ArrayList<NodeMeta> getVisited() {
        return visited;
    }

    public String toString(){
        String output = "Final Node: "+finalNode;
        return output;
    }

}
