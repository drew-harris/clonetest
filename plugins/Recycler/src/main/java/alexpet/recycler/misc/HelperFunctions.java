package alexpet.recycler.misc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HelperFunctions {

    // public static void addToStorage(HashMap<String,Integer> storage,String key){
    // storage.add
    // }
    public static int GCD(int a, int b) {
        // euclids algorithm for GCD; idk guy was smart
        int r;
        while (b != 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;// if a is 1; there is no non-trivial solution
    }

    public static int GCDofList(ArrayList<Integer> nums) {
        int result = nums.get(0);
        for (int num : nums) {
            result = HelperFunctions.GCD(result, num);
            if (result == 1) {
                return 1;
            }

        }
        return result;
    }

    public static List<Entity> getNearbyEntities(Location location, double radius) {
        List<Entity> entities = (List<Entity>) location.getWorld().getNearbyEntities(location, radius, radius, radius);
        return entities;
    }

    public static boolean checkEntity(Entity test, EntityType target) {
        if (test.getType() == target) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<WeightedNode> fileToList() {
        ArrayList<WeightedNode> retVal = new ArrayList<>();
        String line = "";
        String splitBy = ";";
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/static/edge_sheet.csv"));
            int index = 0;

            while ((line = br.readLine()) != null) // returns a Boolean value
            {
                // System.out.println("WE ARE INSIDE THE WHILE LOOP");
                if (index != 0) {
                    String[] lineSplit = line.split(splitBy); // use ; as separator
                    WeightedNode tempNode = new WeightedNode(lineSplit[0], lineSplit[1],
                            Integer.parseInt(lineSplit[2]));
                    // System.out.println("Employee [First Name=" + employee[0] + ", Last Name=" +
                    // employee[1] + ", Designation=" + employee[2] + ", Contact=" + employee[3] +
                    // ", Salary= " + employee[4] + ", City= " + employee[5] +"]");
                    retVal.add(tempNode);
                }
                index++;

            }
        } catch (Exception e) {
            // System.out.println(e);
            System.out.println("ERROR WITH THE FILE");
        } finally {
            System.out.println("GRAPH SIZE: " + retVal.size());
            return retVal;
        }

    }

    public static HashMap<String, ArrayList<WeightedNode>> initTargets(ArrayList<WeightedNode> graph) {
        HashMap<String, ArrayList<WeightedNode>> pointsTo = new HashMap<>();
        // the string is what is getting pointed to
        for (WeightedNode node : graph) {
            String target = node.getTarget();
            if (pointsTo.containsKey(target)) {
                ArrayList<WeightedNode> valuePair = pointsTo.get(target);
                valuePair.add(node);
            } else {
                ArrayList<WeightedNode> temp = new ArrayList<>();
                temp.add(node);
                pointsTo.put(target, temp);
            }

        }
        return pointsTo;
    }

}
