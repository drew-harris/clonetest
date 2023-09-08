package alexpet.recycler.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RecyclerBackEnd {
    HashMap<String, Integer> storage = new HashMap<>();

    ArrayList<HashMap<String, Integer>> storageList = new ArrayList<>();

    // each hashmap contains the item being recycled for example in the stick
    // recycled to plank example -- > it contains sticks until it can produce the
    // planks
    public void addStorage() {
        storageList.add(new HashMap<>());
    }

    public int getlengthOfStorage() {
        return storageList.size();
    }

    // A note about should produce, the hashmap being passed could very well be a
    // completly different size with different elements
    // basically dont forget that the recyler can produce different things so it
    // needs to keep track of that in some way; maybe store the number of sticks
    // passing through;-->so like is they give a stick store .25 plank? or that
    // there is one stored stick?-->probably that there is one stored stick
    public boolean shouldProduce(/* HashMap<String,Integer> storage, */String key, int valNeeded, int recyclerNumber) {
        // key is a string because i am only adding to one key
        // amountsneeded is a hashmap because it has to check all the differnt ones and
        // know which they correspond too-->no its valNeeded as an integer because it
        // can only produce one output (crafting cannot have multiple outputs)
        HashMap<String, Integer> recyclerStrg = storageList.get(recyclerNumber);

        // DONT FORGET TO ADD THE EDGE CASE OF THE KEY NOT EXISTING IN THE LIST
        // AND WHAT HAPPENS IF I GET A NON EXISTENT KEY
        if (!recyclerStrg.containsKey(key)) {
            recyclerStrg.put(key, 1);
            storageList.set(recyclerNumber, recyclerStrg);
            return false;
        }

        int retrivedVal = recyclerStrg.get(key);
        retrivedVal += 1;
        if (retrivedVal < valNeeded) {
            recyclerStrg.replace(key, retrivedVal);
            storageList.set(recyclerNumber, recyclerStrg);
        } else {
            recyclerStrg.remove(key);
            storageList.set(recyclerNumber, recyclerStrg);
            // ShouldProduceOutput output = new ShouldProduceOutput(storage,true);
            // return output;
            return true;
        }
        // ShouldProduceOutput output = new ShouldProduceOutput(storage,false);
        // return output;
        return false;

        // if false is returned the value will have to be updated
    }

    public String getRandomElement() {
        List<String> keysAsArray = new ArrayList<String>(storage.keySet());
        int size = keysAsArray.size();
        Random rand = new Random();
        int index = rand.nextInt(size);
        // return index;
        return keysAsArray.get(index);

    }

}
