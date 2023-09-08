package alexpet.recycler.misc;

import java.util.HashMap;

public class ShouldProduceOutput {
    public HashMap<String, Integer> storage = new HashMap<>();
    public boolean shouldProduce;

    public ShouldProduceOutput(HashMap<String, Integer> storage, boolean shouldProduce) {
        this.storage = storage;
        this.shouldProduce = shouldProduce;
    }
}
