package alexpetmecky.contraption.misc;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;


import java.io.*;

import java.util.*;

public class HelperFunctions {
    
    public static double findAmountPerPath(LinkedList<NodeMeta> path){
        //this is the amount that are required for 1 unit of output,
        // even if the origional recipe produces more than 1 unit, it will scale


        double amount=1.0;
        //the path is backword, this is the
        Iterator<NodeMeta> listIterator = path.descendingIterator();
        while (listIterator.hasNext()){
            //System.out.println(listIterator.previous());
            NodeMeta currNode = listIterator.next();
            if(currNode.getTarget().contains("r_")){
                //recipeNode
                amount = amount / currNode.getWeight();
                System.out.println("Divided by "+currNode.getWeight()+" -- Current amount: "+amount);
                System.out.println(currNode.getSource());
            }else{
                amount = amount * currNode.getWeight();
                System.out.println("Multipled by "+currNode.getWeight()+" -- Current amount: "+amount);
                System.out.println(currNode.getSource());
            }
        }

        return Math.abs(amount);
    }

    public static HashMap<String, Double> getFinalElement(LinkedList<NodeMeta> path){
        NodeMeta finalNode = path.get(0);
        HashMap<String,Double> retVal =new HashMap<>();
        retVal.put(finalNode.getTarget(), (double) finalNode.getWeight());
        return retVal;
    }
    public static HashMap<String,Double> findPathAmountminus2(LinkedList<NodeMeta> path){
        double amount = 1;
        Iterator<NodeMeta> listIterator = path.descendingIterator();
        int index = path.size();
        String targetNodeFinal = null;

        HashMap<String,Double> retVal = new HashMap<>();

        while (listIterator.hasNext() && index > 2){
            NodeMeta currNode = listIterator.next();
            if(currNode.getTarget().contains("r_")){
                //recipeNode

                amount = amount / currNode.getWeight();
                System.out.println("Divided by "+currNode.getWeight()+" -- Current amount: "+amount);
                System.out.println(currNode.getSource());
                if(currNode.getSource() !=null){
                    targetNodeFinal = currNode.getTarget();
                }

            }else{
                amount = amount * currNode.getWeight();
                System.out.println("Multipled by "+currNode.getWeight()+" -- Current amount: "+amount);
                System.out.println(currNode.getSource());

                if(currNode.getSource() !=null){
                    targetNodeFinal = currNode.getTarget();
                }

            }
            index --;

        }

        retVal.put(targetNodeFinal,amount);
        return retVal;


    }
    public static double costFromPrevToFinal(LinkedList<NodeMeta> path){
        //this will find and return he cost from the second to last item to the last item

        //go forward 2 elements
        double amount =1;
        //Iterator<NodeMeta> iterator = path.();
        int i = 0;
        while(i<path.size() && i <2){
            NodeMeta currNode = path.get(i);
            if(currNode.getTarget().contains("r_")){
                //recipeNode

                amount = amount / currNode.getWeight();
                System.out.println("Divided by "+currNode.getWeight()+" -- Current amount: "+amount);
                System.out.println(currNode.getSource());
            }else{
                amount = amount * currNode.getWeight();
                System.out.println("Multipled by "+currNode.getWeight()+" -- Current amount: "+amount);
                System.out.println(currNode.getSource());
                //targetNodeFinal = currNode.getTarget();

            }
            i++;

        }
        return amount;

    }

    public static void printPath(LinkedList<NodeMeta> path){
        for(NodeMeta node:path){
            System.out.println(node);
        }
    }
    public static int GCD(int a ,int b){
        //euclids algorithm for GCD;
        int r;
        while(b!=0){
            r= a%b;
            a=b;
            b=r;
        }
        return a;//if a is 1; there is no non-trivial solution
    }

    public static int GCDofList(ArrayList<Integer> nums){
        int result = nums.get(0);
        for(int num:nums){
            result = HelperFunctions.GCD(result,num);
            if(result ==1){
                return 1;
            }

        }
        return result;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Entity> getNearbyEntities(Location location,double radius){
        //List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 15, 15, 15);
        //List<Entity> entities
        //return entities;
        //location.getWorld().getNearbyEntities()
        List<Entity> entities =(List<Entity>)location.getWorld().getNearbyEntities(location, radius, radius, radius);
        //Collections<Entity> entities =location.getWorld().getNearbyEntities(location, radius, radius, radius, (entity) -> entity.getType() == EntityType.ITEM_FRAME);
        return entities;
    }

    public static boolean checkEntity(Entity test,EntityType target){
        if(test.getType() == target){
            return true;
        }else{
            return false;
        }
    }
    public static boolean checkBlock(Block test, Material target){
        if(test.getType() == target){
            return true;
        }else{
            return false;
        }
    }
    public static LinkedList<NodeMeta> generatePath(SearchReturn searchVal){

        NodeMeta finalNode = searchVal.getFinalNode();
        ArrayList<NodeMeta> allNodes = searchVal.getVisited();
        LinkedList<NodeMeta> path = new LinkedList<>();

        path.addFirst(finalNode);

        while (finalNode.getParent() != null){
            for(NodeMeta node:allNodes){

                if(finalNode.getParent().equals(node)){
                    finalNode = node;
                    path.addFirst(finalNode);
                    break;
                }
            }
        }
        return path;


    }
    public static ArrayList<WeightedNode> fileToList(){
        ArrayList<WeightedNode> retVal = new ArrayList<>();
        String line = "";
        String splitBy = ";";
        try{
            BufferedReader br = new BufferedReader(new FileReader("/static/edge_sheet.csv"));
            int index = 0;

            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                //System.out.println("WE ARE INSIDE THE WHILE LOOP");
                if(index !=0){
                    String[] lineSplit = line.split(splitBy);    // use ; as separator
                    WeightedNode tempNode = new WeightedNode(lineSplit[0],lineSplit[1],Integer.parseInt(lineSplit[2]));
                    //System.out.println("Employee [First Name=" + employee[0] + ", Last Name=" + employee[1] + ", Designation=" + employee[2] + ", Contact=" + employee[3] + ", Salary= " + employee[4] + ", City= " + employee[5] +"]");
                    retVal.add(tempNode);
                }
                index++;

            }
        }catch (Exception e){
            //System.out.println(e);
            System.out.println("ERROR WITH THE FILE");
        }finally {
            System.out.println("GRAPH SIZE: "+retVal.size());
            return retVal;
        }



    }

    public static HashMap<String,ArrayList<WeightedNode>> initTargets(ArrayList<WeightedNode> graph){
        HashMap<String,ArrayList<WeightedNode>> pointsTo = new HashMap<>();
        //the string is what is getting pointed to
        for(WeightedNode node:graph){
            String target = node.getTarget();
            if(pointsTo.containsKey(target)){
                ArrayList<WeightedNode> valuePair= pointsTo.get(target);
                valuePair.add(node);
            }else{
                ArrayList<WeightedNode> temp = new ArrayList<>();
                temp.add(node);
                pointsTo.put(target,temp);
            }

        }
        return pointsTo;
    }

    public static String[][] makeHyperMatrix(String filepath){
        //String retVal[][];
        //File hyperFile = new File(filepath);
        FileInputStream fis;
        //String[][] retVal = new String[0][];
        String[][] retVal;
        try{
            fis = new FileInputStream(filepath);
            DataInputStream hyperFile = new DataInputStream(fis);


            //BufferedReader br = new BufferedReader(new FileReader(filepath));
            ///String line = br.readLine();

            List<String[]> lines = new ArrayList<String[]>();
            String thisLine;
            while((thisLine = hyperFile.readLine()) != null){
                lines.add(thisLine.split(","));
            }

            retVal = new String[lines.size()][0];
            //does this create a memory leak?is it just overwritten
            lines.toArray(retVal);
            fis.close();
            return retVal;
        }catch (Exception e){
            //System.out.println("ERROR FILE NOT FOUND");
            //System.out.println("PLUGIN WILL NOT WORK");
            //throw e;
            //return;
            System.out.println(e);
            return null;
        }




        //return retVal;
        //return -1;
    }

    public static String[][] makeHyperMatrixTranspose(String[][] matrix){

        int m = matrix.length;
        int n = matrix[0].length;

        String[][] transposedMatrix = new String[n][m];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }

        return transposedMatrix;
        //return retVal;
    }

    public static boolean compareArrays(){

        return true;
    }

    public static void searchBreakDown(String itemToSearch, ArrayList<ItemStack> reqOutput,HyperApi api){

        //gets the string value of the items, then sorts them alphabeltically
        ArrayList<String> reqOutputStringList = new ArrayList<>();
        for(ItemStack o:reqOutput){
            String temp=String.valueOf(o.getType());
            reqOutputStringList.add(temp);
        }
        Collections.sort(reqOutputStringList);


        //preforms a search one deep, gets the string values, sorts alphabetically
        ArrayList<Recipe> firstSearch= api.findIngredientsNew(itemToSearch);
        for (Recipe r:firstSearch) {
            //each recipe (r) is a different breakdown of the recipe;
            //it contains an arraylist and a string
            ArrayList<ItemBreakdown> items = r.getItemsRequired();
            ArrayList<String> itemsString = new ArrayList<>();
            for (ItemBreakdown item:items) {
                itemsString.add(String.valueOf(item.getItem()));
            }
            Collections.sort(itemsString);

            //boolean checkVal = Arrays.equals(reqOutputStringList,itemsString);
            //compares the 2 arrays to check if the given == required output
            boolean checkVal = reqOutputStringList.equals(itemsString);
            if(checkVal){
                return;//idk what to return yet
            }else{

                //creates and arraylist of all indecies that do not match to search them
                ArrayList<Integer> indeciesToContinue = deepCheck(reqOutputStringList,itemsString);
                ArrayList<String> copyOfItemsString = new ArrayList<>(itemsString);//copies itemsString so origional can be manipulated

                //for every non-matching index search it
                ArrayList<String> subSearchItemsString = new ArrayList<>();

                //a loop will be needed here (here meaning around the for(int:indeciesToContinue), with 2 ending conditions
                //1-indecies to continue is empty because all items have been successfully broken down, in which case this whole thing returns true, or somehting along those lines, depending on what the return should be
                //2-an element cannot be broken down and the users recipe is invalid, in which case the function needs to return some version of false
                //LOOK HERE FOR TODO
                while( !indeciesToContinue.isEmpty()  /*SOME CONDITION THAT CHECKS IF THE ELEMENT CAN BE BROKEN DOWN OR NOT*/) {
                    //there are 2 things to check to see if an element fails to be broken down
                        //1-(the easy check) it cannot be broken down more
                        //2-we are stuck in an infinate loop ex: iron ingot->iron nuggets->iron ingot->etc (bc both are crafting recipes)

                    //FIXME--> the while loop is looking at indeciesToContinue and the forloop directly below loops through indeciesToContinue
                    //Somehow indeciesToContinue or something needs to be updated in such a way that after the first iteration it changes so it breaksdown the elements inside of it and not the origional elements that were broken down in the previous iteration


                    for (int index : indeciesToContinue) {
                        String newSearch = copyOfItemsString.get(index);
                        itemsString.remove(index);//remove the item at the index that is about to be searched

                        //the result of every search should then be put back into itemsString and recompared to the origional after the whole thing has searched
                        ArrayList<Recipe> subsearch = api.findIngredientsNew(newSearch);


                        //take the search that just happened--> go into all of the different recipes
                        //if one of those recipes has the end results, confirm that and move on to the next thing
                            //--> to be edited, if these are not the right searches for a particular thing they need to be broken down more-->make sure i do not got back and rebreakdown the same things
                            //LOOK HERE FOR TODO
                        for (Recipe rec : subsearch) {
                            //if(rec.getItemsRequired() );
                            int breakDownAmount = rec.getItemsRequired().size();
                            int inArrayCount = 0;
                            for (ItemBreakdown item : rec.getItemsRequired()) {
                                if (!itemsString.contains(item)) {
                                    break;
                                } else {
                                    inArrayCount++;
                                }
                            }
                            if (inArrayCount == breakDownAmount) {
                                //breakdown is good--> replace the items in the array that need to br broken down
                                //basically all items in this iteration of the broken down search are in the final recipe
                                //when it does get here, the index that was not checked off gets remove and not checked again
                                indeciesToContinue.remove(indeciesToContinue.indexOf(index));//index is the index in the required output that does not match
                            }

                        }


                    }

                }//this closes off the big loop that checks if the indecies are empty


            }


        }

    }

    public static void recChecks(){

    }

    public static ArrayList<Integer> deepCheck(ArrayList<String> ending, ArrayList<String> searching){
        //compare the arrays and see if any of the elements are the same
        //it returns all indecies that are not the same
        ArrayList<Integer> indeciesToSearch = new ArrayList<>();
        int index=0;
        for(String s:searching){
            if(!ending.contains(s)){
                indeciesToSearch.add(searching.indexOf(s));
            }
        }
        return indeciesToSearch;
    }

    public static void createGraph(){

    }



}
