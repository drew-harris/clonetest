package alexpetmecky.contraption.misc;

import java.util.ArrayList;
import java.util.HashMap;

public class ContraptionStorage {

    //HashMap<String,Integer> amountNeededPerSet = new HashMap<>();//this will be in reference to the amountMadeBeforeFinal
                                                //so in the example of logs to pickaxe, it should store stick:2, iron_ingot:3
    boolean isRecycler=false;
    HashMap<String,Double> amountProducedPerItem = new HashMap<>(); //this is how many outputs 1 input makes; example(1 oak log makes 4 pickaxes, 1 iron_ingot makes .33333 pickaxes)

    //HashMap<String,Double> amountMadeBeforeFinal = new HashMap<>();//this is going to be 1 item node before the final output item
                                                    //so producing logs to pickaxes; this will store sticks:8

    HashMap<String,Double> currStorage = new HashMap<>(); //this will also store the items tracked amountMadeBeforeFinal
                                        //in the example of logs and iron ingots to pickaxes; it will store sticks and ingots
                                        //for a recycler this will store if something cannot produce to a whole, in the example of a pickaxe to sticks and iron, it will hold sticks and iron

    HashMap<String,Double> amountProducedPerSet = new HashMap<>(); //this keeps how many of an item are created, for crafting it will normally just store 1 item, byt for recycling it will store all the componenets and the amount made

    String itemBeingRecycled; //this is specific to recycling within the contraption

    //public void setAmountNeededPerSet(String item,int amount){
        //amountNeededPerSet.put(item,amount);
    //}
    public void setAmountProducedPerItem(String item,double amount){

        amountProducedPerItem.put(item,amount);
    }
    public HashMap<String, Double> getAmountProducedPerItem(){
        return amountProducedPerItem;
    }
    public HashMap<String, Double> getAmountProducedPerSet(){
        return amountProducedPerSet;
    }
    public void setAmountProducedPerSet(String item,double amount){
        amountProducedPerSet.put(item,amount);
    }
    public void setCurrStorage(String item){
        currStorage.put(item,0.0);
    }
    public HashMap<String,Double> getCurrStorage(){
        return currStorage;
    }
    public void updateStored(String name, double amount){
        double storedAmount = currStorage.get(name);
        storedAmount +=amount;
        currStorage.put(name,storedAmount);
    }

    public void recycleSetInput(String name){
        itemBeingRecycled = name;
    }
    public String getRecycleInput(){
        return itemBeingRecycled;
    }
    public boolean checkRecycler(){
        return isRecycler;
    }
    public void changeBackendState(boolean isRecycler){
        this.isRecycler = isRecycler;
    }
    public boolean shouldProduceRecycle(String name){
        //ill use this to check if the output is the stored item
        if(name.equals(itemBeingRecycled)){
            return true;
        }else{
            return false;
        }
    }
    public HashMap<String, Double> produceRecycle(){
        HashMap<String,Double> produced= new HashMap<>();
        //System.out.println("Inside the produceRecycle function");

        for(String key:amountProducedPerItem.keySet()){
            //System.out.println("amount produced per set: "+key+": "+amountProducedPerItem.get(key));
            double currAmount = currStorage.get(key);
            //System.out.println("CurrAmount: "+key+": "+currAmount);
            currAmount = currAmount + amountProducedPerItem.get(key);
            //System.out.println("New Curr: "+key+": "+currAmount);

            double dec = currAmount %1;
            double wholeNum = currAmount - dec;


            produced.put(key,wholeNum);
            //System.out.println("Producing: "+key+": "+wholeNum);
            currStorage.put(key,dec);
            //System.out.println("Amount being stored: "+key+": "+dec);

            //currStorage.put(key,currAmount);
        }
        return produced;




       //return amountProducedPerSet;


    }
    public boolean shouldProduce(){
        //System.out.println("CURRSTORAGE SIZE: "+currStorage.size());
        double amountNeeded;

        for(String key:currStorage.keySet()){
            //System.out.println("key: "+key);
            amountNeeded = 1.0 / amountProducedPerItem.get(key);
            //System.out.println("Amount needed: "+amountNeeded);
            //System.out.println("is: "+ currStorage.get(key) +" < " +amountNeeded);

            if(amountNeeded < 0){
                //im not sure but this might show it is crafting
                if (currStorage.get(key) < Math.abs(amountNeeded)){

                    return false;
                }
            }else{
                if (currStorage.get(key) < amountNeeded){

                    return false;
                }

            }


        }
        return true;

    }

    public HashMap<String, Double> produce(){//should return a hashmap of all the items that can produce
        //something to keep in mind, when crafting i just need to subtract from currStroage and return the amountProducedPerSet
        //when recycling I need to do it enough times that it gives all outputs, this may be solved by the prev math, check it

        System.out.println("PRODUCE RAN");
        for(String key:currStorage.keySet()){
            double amountNeeded = 1 / amountProducedPerItem.get(key);//this may not be right because it assumes that it produces 1 item per , but in recycling it may produce mroe
            System.out.println("amount needed="+amountNeeded);
            double newAmount = currStorage.get(key)-amountNeeded;//could try multipiying this, but i would need to make sure there is enough off all the other items
            System.out.println("key: "+key+" new amount: "+newAmount);
            currStorage.put(key,newAmount);
        }

        return amountProducedPerSet;
    }











}
