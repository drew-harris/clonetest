package alexpetmecky.contraption.misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Math.abs;

public class HyperApi {
    String [][] HYPER_GRAPH;
    String [][] HYPER_GRAPH_T;
    String[] ITEM_AT_COLUMN;

    ArrayList<WeightedNode> graph =HelperFunctions.fileToList();

    HashMap<String,ArrayList<WeightedNode> > targets = HelperFunctions.initTargets(graph);
    public HyperApi(String file){
/*
        HYPER_GRAPH = HelperFunctions.makeHyperMatrix(file);
        if(HYPER_GRAPH==null){
            System.out.println("HYPER GRAPH MATRIX NOT CREATED????");
        }else{
            HYPER_GRAPH_T = HelperFunctions.makeHyperMatrixTranspose(HYPER_GRAPH);
        }
        ITEM_AT_COLUMN = HYPER_GRAPH_T[0];
*/
        //graph = HelperFunctions.fileToList();

    }


    public SearchReturn searchGraphSingle(String start, String end){
        //where start is iron pick for example
        //System.out.println("End: "+end);
        //System.out.println("Start: "+start);

        LinkedList<NodeMeta> queue = new LinkedList<>();
        ArrayList<NodeMeta> visitedItems = new ArrayList<>();
        start = "minecraft:"+start;
        end = "minecraft:"+end;
        //System.out.println("start: "+start);
        //System.out.println("Starting init");
        for(WeightedNode node:graph){
            if(node.getTarget().equalsIgnoreCase(start)){

                NodeMeta initialNode = new NodeMeta(node.getSource(),node.getTarget(),node.getWeight(),false,null);
                visitedItems.add(initialNode);
                queue.addLast(initialNode);
                //System.out.println("Node Init: "+initialNode.toString());

            }
        }

        //System.out.println("end init");

        //System.out.println("starting search");
        System.out.println(queue.size());
        while (queue.size() != 0){
            NodeMeta currNode = queue.removeFirst();
            //System.out.println("Current: "+currNode.toString());
            if(currNode.getSource().equalsIgnoreCase(end)){
                SearchReturn ret = new SearchReturn(currNode,visitedItems);
                //System.out.println("FOUND");
                return ret;
            }

            ArrayList<WeightedNode>children=targets.get(currNode.getSource());
            if(children != null){
                for(WeightedNode child:children){
                    //targets.get(currNode.getSource())
                    //.convertToWeighted()
                    boolean visited =false;

                    for(NodeMeta visNode:visitedItems){
                        if(child.equals(visNode)){
                            visited = true;
                            break;
                        }
                    }
                    if(!visited){
                        NodeMeta childNode = new NodeMeta(child,false,currNode);
                        visitedItems.add(childNode);
                        //queue.addFirst(childNode);
                        queue.addLast(childNode);
                        //System.out.println("Added: "+childNode.toString());
                    }


                }


            }


        }

        //NodeMeta falseNode = new NodeMeta(null,null,null,null);
        System.out.println("NOT FOUND");
        return null;

    }

/*
    public LinkedList<WeightedNode> searchGraphSingle(String start,String end){
        //where start is iron pick for example
        LinkedList<NodeMeta> queue = new LinkedList<>();
        ArrayList<WeightedNode> visitedItems = new ArrayList<>();

        ArrayList<WeightedNode>stackItems =targets.get(start);//gets the kids of start



        //where start is iron pick for example
        LinkedList<WeightedNode> stack = new LinkedList<>();
        ArrayList<WeightedNode> visitedItems = new ArrayList<>();
        LinkedList<WeightedNode> path = new LinkedList<>();

        ArrayList<WeightedNode>stackItems =targets.get(start);

        for(WeightedNode node:stackItems){
            stack.addFirst(node);
        }

        while(stack.size()!=0){
            WeightedNode currNode = stack.getFirst();
            path.add(currNode);
            ArrayList<WeightedNode> pointsToCurr = targets.get(currNode.getSource());


            for(WeightedNode node:pointsToCurr){
                for(WeightedNode visited:visitedItems) {
                    if((node.getSource().equals(visited.getSource())) &&(node.getTarget().equals(visited.getTarget()))&&(node.getWeight() == visited.getWeight()) ){
                        System.out.println("Already Visited");
                    }else {
                        if(node.getSource().equals(end)){
                            return path;
                        }else{
                            ArrayList<WeightedNode> pointsToNew = targets.get(node.getSource());
                            for(WeightedNode temp:pointsToNew){
                                stack.addFirst(temp);
                            }
                        }
                    }

                }

                }

            }
            return path;

        }
        */

/*

        if(start.equals(end)){
            return;
        }
        LinkedList<WeightedNode> stack = new LinkedList<>();
        LinkedList<WeightedNode> queue = new LinkedList<>();

        ArrayList<WeightedNode> visitedItems = new ArrayList<>();
        ArrayList<WeightedNode> visitedRecipeNodes = new ArrayList<>();

        ArrayList<WeightedNode> path = new ArrayList<>();

        HashMap <WeightedNode,Boolean> visited= new HashMap<>();
        //HashMap <String,ArrayList<String>> pointsTo = new HashMap<>();

        for (WeightedNode node:graph) {
            visited.put(node,false);


            if(node.getTarget().equals(start)){
                //String source = node.getSource();
                queue.add(node);
            }

        }

        //stack.addFirst();
        while(queue.size() !=0){
            WeightedNode queueElem= queue.pollFirst();

            ArrayList<WeightedNode> pointsTo = targets.get(queueElem.getSource());
            //pointsTo is an arraylist of all elements that points to the elem in queue
            for (WeightedNode node:pointsTo){
                //stack.addFirst(node);
            }

        }





    }
    */
    public void searchGraph(/*ArrayList<String> input,String output*/){
        Graph myGraph = new Graph("/static/edge_sheet.csv");
        List<String> path =  myGraph.shortestPath("minecraft:oak_log","minecraft:stick");
        for(String item:path){
            System.out.println(item);
        }
        /*
        LinkedList<WeightedNode> queue = new LinkedList<WeightedNode>();
        ArrayList<WeightedNode> visitedItems = new ArrayList<>();
        ArrayList<WeightedNode> visitedRecipeNodes = new ArrayList<>();

        ArrayList<String> inputEdit = input;

        for (WeightedNode node:graph) {
            if(node.getTarget().equals(output)){
                //String source = node.getSource();
                queue.add(node);
            }

        }

        while(queue.size()!=0){
            for(WeightedNode currNode:queue){
                String source = currNode.getSource();
                String first2 = source.length() < 2 ? source : source.substring(0, 2);

                if(first2.equals("r_")){

                }else{

                }

            }

        }




        ArrayList<String> matchingItems;

        ArrayList<WeightedNode> sourcesInitial = new ArrayList<>();
        for (WeightedNode node:graph) {
            if(node.getTarget().equals(output)){
                //String source = node.getSource();
                sourcesInitial.add(node);
            }

        }

        for (WeightedNode node:sourcesInitial) {
            String source = node.getSource();
            ArrayList<WeightedNode> allTargets = new ArrayList<>();


            for (WeightedNode tempNode : graph) {
                if (node.getTarget().equals(source)) {
                    //String source = node.getSource();
                    allTargets.add(node);
                }

            }

        }


*/
    }


    public void makeGraph(){
        String node_list[] = this.ITEM_AT_COLUMN;
        //edgelist is the same as the HyperGraph
        int hyperGraphIndex=0;

        //ArrayList<ArrayList<Node>> recipeList = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Node>>> recipeList= new ArrayList<>();
        //[ [ [Node],[Node,Node] ],[]  ]
        for(String[] recipe:this.HYPER_GRAPH_T){
        //loop through the transposed hypergraph

            if(hyperGraphIndex !=0 ){
                //skipping the first row in the transposed hypergraph bc it contains names

                //String[][] ingredints;
                //String[][] craft;

                //ArrayList<String[]> ingredients;
                //ArrayList<String[]> craft;

                ArrayList<Node> ingredients = new ArrayList<>();
                ArrayList<Node> craft= new ArrayList<>();

                int nodeListIndex=0;
                for(String amount:recipe){
                    int amountNum = Integer.parseInt(amount);
                    if(amountNum <0){
                        //String[] temp = {node_list[nodeListIndex],amountNum};
                        Node temp = new Node(node_list[nodeListIndex],amountNum);
                        ingredients.add(temp);
                    }else if(amountNum >0){
                        Node temp = new Node(node_list[nodeListIndex],amountNum);
                        craft.add(temp);

                    }

                    nodeListIndex +=1;
                }

                if(craft.size() == 0){
                    System.out.println("Uncraftable");
                }else{
                    //Node[] tempNodeList = {craft,ingredients};
                    ArrayList<ArrayList<Node>> tempNodeList= new ArrayList();
                    //Arrays.asList(craft,ingredients);
                    tempNodeList.add(craft);
                    tempNodeList.add(ingredients);

                    //(craft,ingredients);
                    //tempNodeList.as
                    recipeList.add(tempNodeList);
                }

            }
            hyperGraphIndex +=1;

        }
        System.out.println("Recipes List Created");

        ArrayList<String> source=new ArrayList<>();
        ArrayList<String> target=new ArrayList<>();
        ArrayList<Integer> weight=new ArrayList<>();

        int recipe_idx=0;
        for (ArrayList<ArrayList<Node>> recipe:recipeList) {

            int i_idx=0;
            for(Node ingredient: recipe.get(1)){
                source.add(ingredient.getName());
                target.add("r_"+recipe_idx);
                weight.add(ingredient.getAmount());
                i_idx+=1;
            }
            source.add("r_"+recipe_idx);
            target.add(recipeList.get(recipe_idx).get(0).get(0).getName());
            weight.add(recipeList.get(recipe_idx).get(0).get(0).getAmount());
            recipe_idx +=1;
        }

        //sheet_representation=[]
        ArrayList<WeightedNode> sheet_representation = new ArrayList<>();
        recipe_idx=0;
        for(String recipe:source){
            //sheet_representation.add();
            WeightedNode tempWeightedNode=new WeightedNode(source.get(recipe_idx), target.get(recipe_idx), weight.get(recipe_idx));
            sheet_representation.add(tempWeightedNode);
            recipe_idx+=1;
        }

        String filename = "edge_sheet.csv";
        try{
            File myFile = new File(filename);

            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write("Source;Target;Weight\n");
                for (WeightedNode tempNode:sheet_representation) {
                    //tempNode.getSource()
                    myWriter.write(tempNode.getSource()+";"+ tempNode.getTarget() +";"+ tempNode.getWeight()+"\n");
                }


            } else {
                System.out.println("File already exists. Overwritting...");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public ArrayList<Recipe> findIngredientsNew(String item_dirty){
        //System.out.println(item);

        String item = "minecraft:"+item_dirty.toLowerCase();//  THIS WILL NEED TO BE CHANGED!!! IT IS NOT CONSISTENT AMOUNG ITEMS IN THE HYPERGRAPH
        System.out.println("ITEM: "+item);
        //THIS MIGHT NOT BE NECESSARY HERE

        ArrayList<Recipe> totalRecipes = new ArrayList<>(); // the return value
        int indexAtItem = -1;
        for(int rowNum=0;rowNum< HYPER_GRAPH.length;rowNum++){
            if(HYPER_GRAPH[rowNum][0].equalsIgnoreCase(item)){
                indexAtItem=rowNum;
            }
        }
        if(indexAtItem==-1){
            System.out.println("ITEM NOT FOUND IN HYPER GRAPH???");
            //create a custom exception here
            return null;
        }
        ArrayList<Integer> columns=new ArrayList<>();
        //columns will hold psitive column numbers
        //a positive column number means that the specific item can be created that many times

        //loop through every column of the hypergraph where the item i need is(skip the first element,its the name)
        //if the number is greater than 0, save that column-->we will need it later
        for(int colNum=1;colNum< HYPER_GRAPH[indexAtItem].length-1;colNum++){
            if(Integer.parseInt(HYPER_GRAPH[indexAtItem][colNum]) > 0){
                columns.add(colNum);
            }
        }


        //columns will become rows as we traverse the transposed matrix
        for(int columnListIndex=0;columnListIndex<columns.size();columnListIndex++){
            //loop through all the indecies of the columns ArrayList
            //use the value at each index as the row for the transposed matrix

            int row = columns.get(columnListIndex).intValue();//column number becomes row number

            //each one of the cols contains a new recipe
            Recipe newRecipe = new Recipe();//creating a new recipe, it will be added to total recipes

            for(int transposedColIndex=0; transposedColIndex<HYPER_GRAPH_T[row].length;transposedColIndex++){
                //loop through the columns at the row of the transposed hypermatrix
                int num = Integer.parseInt(HYPER_GRAPH_T[row][transposedColIndex]);
                String ingred_name = ITEM_AT_COLUMN[transposedColIndex];

                if(num > 0){
                    //nothing needed rn
                    //this is how many of the item can be created
                    ItemBreakdown main = new ItemBreakdown(ingred_name,num);
                    newRecipe.setMain(main);
                }

                if(num < 0){
                    //this is how many of a particular item is needed to make it
                    //REMEMBER num IS A NEGATIVE NUMBER
                    ItemBreakdown recipePart = new ItemBreakdown(ingred_name,abs(num));
                    newRecipe.addItemsRequired(recipePart);

                }



            }
            totalRecipes.add(newRecipe);

        }




        //Recipe[] retVal = null;
        return totalRecipes;
    }
    public ArrayList<HashMap<Integer,HashMap<String,Integer>>> findIngredients(String item){
        ArrayList<HashMap<Integer,HashMap<String,Integer>>> retVal=null;
        int index=0;
        for(int rowNum = 0; rowNum<HYPER_GRAPH.length;rowNum++){
            if(HYPER_GRAPH[rowNum][0].equals(item)){
                index=rowNum;
                break;
            }
        }

        int column = 1;
        //String columns[];
        ArrayList<Integer> columns = new ArrayList<Integer>();


        for(int i =1; i<HYPER_GRAPH[index].length-1;i++){
            //i will be the index in the HYPER_GRAPH row
            //it starts at 1 to skip the first element

            if( Integer.parseInt(HYPER_GRAPH[index][i]) > 0){
                columns.add(i);
            }

        }

        for(int rowIndex=0; rowIndex <columns.size();rowIndex++ ){
            //this will loop through the Transposed matrix
            int amount_created=0;
            int itemIndex = 0;

            //ArrayList<String> tempList = new ArrayList<String>();
            HashMap<String, Integer> tempMap= new HashMap<String,Integer>();

            int Hyper_Graph_T_Col = 0;
            String[] row = HYPER_GRAPH_T[rowIndex];

            for(int numberIndex=0; numberIndex<row.length;numberIndex++){
                int number = Integer.parseInt(row[numberIndex]);
                if(number >0 ){
                    //number of item that can be made
                    amount_created = number;
                }
                if(number<0){
                    //number of items required to make the item
                    //HashMap<String, Integer> tempMap= new HashMap<String,Integer>();
                    tempMap.put(ITEM_AT_COLUMN[itemIndex],abs(number));//itemIndex goes through each

                }
                itemIndex+=1;
            }
            if(!tempMap.isEmpty()){
                //HashMap<Integer,HashMap<String,Integer>> interMap;
                HashMap<Integer,HashMap<String,Integer>> interMap=null;
                interMap.put(amount_created,tempMap);
                retVal.add(interMap);



            }
        }

        return retVal;
    }//closes the findIngredients method


}
