package alexpetmecky.contraption.misc;

import alexpetmecky.contraption.Contraption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ContraptionBackend {
    //ArrayList<HashMap<String,Integer>> storageList =new ArrayList<>();

    ArrayList<ContraptionStorage> storageList = new ArrayList<>();//i think i am going to use this

    public ContraptionBackend() {

    }
    public boolean checkRecycler(int contraptionIndex){
        ContraptionStorage curr = storageList.get(contraptionIndex);
        return curr.checkRecycler();
    }
    public void setRecyclerInput(int contraptionIndex,String item){
        //##
        ContraptionStorage curr = storageList.get(contraptionIndex);
        curr.recycleSetInput(item);
        //storageList.add(contraptionIndex,curr);
        storageList.set(contraptionIndex,curr);
        System.out.println("setRecyclerInput Lenght of storageList: "+storageList.size());

    }
    public String getRecycleInput(int contraptionIndex){
        ContraptionStorage curr = storageList.get(contraptionIndex);
        String recycledItem = curr.getRecycleInput();
        return recycledItem;
    }


    public HashMap<String,Double> produceRecycled(int contraptionIndex){
        //##
        ContraptionStorage curr = storageList.get(contraptionIndex);
        HashMap<String,Double> produced = curr.produceRecycle();
        //storageList.add(contraptionIndex,curr);
        storageList.set(contraptionIndex,curr);
        System.out.println("produceRecycled Length of storageList: "+storageList.size());
        return produced;

    }
    public boolean shouldProducedRecycled(int contraptionIndex,String item){
        ContraptionStorage curr = storageList.get(contraptionIndex);
        boolean shouldProduceRecycle = curr.shouldProduceRecycle(item);
        //dont need to put back because shouldProduce does not change anything in the backend
        System.out.println("shouldProducedRecycled Length of storageList: "+storageList.size());
        return shouldProduceRecycle;

    }
    public void setIsRecycler(int contraptionIndex, boolean isRecycler){
        //##
        ContraptionStorage curr = storageList.get(contraptionIndex);
        System.out.println("isRecycler Boolean: "+isRecycler+" for recycler: "+contraptionIndex);
        curr.changeBackendState(isRecycler);
        //storageList.add(contraptionIndex,curr);
        storageList.set(contraptionIndex,curr);
        //System.out.println("setIsRecycler Lenght of storageList: "+storageList.size());
    }

    public void addStorage(){
        //HashMap<String,Integer> storageMap = new HashMap<>();
        ContraptionStorage newStorage = new ContraptionStorage();
        storageList.add(newStorage);
        //System.out.println("addStorage Lenght of storageList: "+storageList.size());


    }

    public int getLength(){
        return storageList.size();
    }


    public boolean shouldProduce(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);

        return currStorage.shouldProduce();

    }
    public HashMap<String, Double> produce(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        return currStorage.produce();
    }
    public void showInventory(int contraptionIndex){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        HashMap<String,Double> storage= currStorage.getCurrStorage();
        for(String key: storage.keySet()){
            System.out.println(key + " "+storage.get(key));
        }
    }
    public void checkItem(int contraptionIndex){
        ContraptionStorage currStorage =  storageList.get(contraptionIndex);
        HashMap<String,Double> storage= currStorage.getCurrStorage();
        for(String key: storage.keySet()){
            System.out.println(key);
        }
    }

    public void insertToProducedPerItem(int contraptionIndex,String item, double amount){
        System.out.println("AMOUNT PRODUCED PER ITEM: "+amount);

        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountProducedPerItem(item,amount);
        storageList.set(contraptionIndex,currStorage);
        //System.out.println("insertToProducedPerItem Length of storageList: "+storageList.size());
    }
    public void insertToAmountProducedPerSet(int contraptionIndex,String item,double amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.setAmountProducedPerSet(item,amount);
        storageList.set(contraptionIndex,currStorage);
        //System.out.println("insertToAmountProducedPerSet Lenght of storageList: "+storageList.size());
    }
    public void insertToNeededPerOutput(int contraptionIndex, String item, int amount){
        //ContraptionStorage currStorage = storageList.get(contraptionIndex);
        //currStorage.setAmountNeededPerSet(item,amount);
        //storageList.set(contraptionIndex,currStorage);
    }
    public void insertToCurrStorages(int contraptionIndex, String item){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        //currStorage.setAmountProducedPerItem(item,0);
        currStorage.setCurrStorage(item);
        storageList.set(contraptionIndex,currStorage);
        //System.out.println("insertToCurrStorages Lenght of storageList: "+storageList.size());
    }

    public void increaseItemInStorage(int contraptionIndex,String item,int amount){
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        currStorage.updateStored(item,amount);
        storageList.set(contraptionIndex,currStorage);
        //System.out.println("insertToCurrStorages Lenght of storageList: "+storageList.size());


    }
    public boolean itemExistsInStorage(int contraptionIndex,String passedItemToCheck){
        System.out.println("Inside EXISTS IN STORAGE");
        //System.out.println()
        ContraptionStorage currStorage = storageList.get(contraptionIndex);
        ////
        ArrayList<String> l = new ArrayList<String>(currStorage.getCurrStorage().keySet());
        for(String key:l){
            System.out.println("Key: "+key);
        }
        /////


        if(currStorage.getCurrStorage().containsKey(passedItemToCheck)){
            return true;
        }else{
            return false;
        }
    }

    public void showContents(){
        for(ContraptionStorage item:storageList){
            System.out.println("CURRSTORAGE: " +item.getCurrStorage());
        }
    }

    public ArrayList<Set<String>> getInputOutputAtLocation(int index){
        ContraptionStorage currStorage = storageList.get(index);
        Set<String> inputKeys = currStorage.getCurrStorage().keySet();

        Set<String> outputKeys = currStorage.getAmountProducedPerSet().keySet();

        ArrayList<Set<String>> retVal = new ArrayList<>();
        retVal.add(inputKeys);
        retVal.add(outputKeys);
        return retVal;


    }

    public ArrayList<Set<String>> getRecyclerInputOutputAtLocation(int index){
        ArrayList<Set<String>> retVal = new ArrayList<>();
        ContraptionStorage currStorage = storageList.get(index);
        String inputKey = currStorage.getRecycleInput();
        Set<String> inputKeys = new HashSet<>();
        inputKeys.add(inputKey);

        Set<String> outputKeys = currStorage.getAmountProducedPerItem().keySet();
        retVal.add(inputKeys);
        retVal.add(outputKeys);


        return retVal;

    }

}
