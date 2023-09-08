package alexpetmecky.contraption.misc;

import java.util.ArrayList;

public class Recipe {
    int amountCreated;

    //ItemBreakdown[] itemsRequired;
    private ItemBreakdown main;//main is the item that was given to be broken down
    private ArrayList<ItemBreakdown> itemsRequired=new ArrayList<ItemBreakdown>();

    Recipe(){
        //default constructor
        //ArrayList<ItemBreakdown> itemsRequired = new ArrayList<ItemBreakdown>();
    }
    Recipe(ItemBreakdown main, ArrayList<ItemBreakdown> itemsRequired){
        this.main=main;
        this.itemsRequired=itemsRequired;


    }

    public void setMain(ItemBreakdown main) {
        this.main = main;
    }

    public void addItemsRequired(ItemBreakdown itemRequired) {
        this.itemsRequired.add(itemRequired);
    }


    public ItemBreakdown getMain(){
        return this.main;
    }

    public ArrayList<ItemBreakdown> getItemsRequired(){
        return this.itemsRequired;
    }


}
