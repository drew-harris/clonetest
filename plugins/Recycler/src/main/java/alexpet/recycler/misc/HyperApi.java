package alexpet.recycler.misc;

import java.util.ArrayList;
import java.util.HashMap;

public class HyperApi {
    ArrayList<WeightedNode> graph = HelperFunctions.fileToList();
    HashMap<String, ArrayList<WeightedNode>> targets = HelperFunctions.initTargets(graph);

    public HyperApi() {
    };

    public ArrayList<WeightedNode> getTargets(String item) {
        // System.out.println(targets.size());
        ArrayList<WeightedNode> items = targets.get(item);
        return items;
    }

}
