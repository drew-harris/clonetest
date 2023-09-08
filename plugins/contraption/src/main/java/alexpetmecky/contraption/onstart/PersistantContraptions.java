package alexpetmecky.contraption.onstart;


import alexpetmecky.contraption.misc.ContraptionBackend;
import alexpetmecky.contraption.misc.ContraptionStorage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.FileReader;

public class PersistantContraptions {
    //this will be called when the class first runs and it will populate several contraptions
    ContraptionBackend backend;
    public PersistantContraptions(ContraptionBackend backend){
        this.backend = backend;
    }
    public ContraptionBackend setContraptions(){

        //ContraptionBackend backend = new ContraptionBackend();
        try{
            //BufferedReader br = new BufferedReader(new FileReader("plugins/contraption/src/main/resources/data.csv"));
            BufferedReader br = new BufferedReader(new FileReader("/static/data.csv"));

            String line;
            String delim = ";";
            br.readLine();//skips first line
            while ((line = br.readLine()) != null) {
            //line="OAK_LOG;OAK_PLANKS;false;4";
            //line="OAK_PLANKS;OAK_LOG;true;.25";
            //boolean active=true;
            //some testing stuff here
            //figure out files on the server then read from there instead once its time to add it
            //while (active) {
                //active=false;
                String[] values = line.split(delim);
                String[] inputs = values[0].split(",");
                String[] outputs = values[1].split(",");
                Boolean isReycler = Boolean.valueOf(values[2]);
                String[] amountPerItem = values[3].split(",");
                        //Float.parseFloat(values[3]);
                System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]+" ");
                int contrapNum = backend.getLength();
                backend.addStorage();//adding a storage
                //isReycler=true;
                if(isReycler){
                    backend.setIsRecycler(contrapNum,true);

                    backend.setRecyclerInput(contrapNum,inputs[0]);
                    for(int i=0;i<outputs.length;i++){
                        backend.insertToProducedPerItem(contrapNum,outputs[i],Double.valueOf(amountPerItem[i]));
                        backend.insertToCurrStorages(contrapNum,outputs[i]);
                    }



                }else{
                    //for crafting contraptions
                    //it works for 1->1 but not yet >1->1
                    if(inputs.length>1){
                        //crafting with more than 1 input
                        backend.insertToAmountProducedPerSet(contrapNum,outputs[0],1);

                        for (int i=0; i<inputs.length;i++){
                            backend.insertToProducedPerItem(contrapNum,inputs[i],Double.valueOf(amountPerItem[i]));
                            backend.insertToCurrStorages(contrapNum,inputs[i]);
                        }



                    }else{
                        //one input to one output
                        backend.setIsRecycler(contrapNum,true);

                        backend.setRecyclerInput(contrapNum,inputs[0]);
                        for(int i=0;i<outputs.length;i++){
                            backend.insertToProducedPerItem(contrapNum,outputs[i],Double.valueOf(amountPerItem[i]));
                            backend.insertToCurrStorages(contrapNum,outputs[i]);
                        }

                    }







                    /*
                    backend.insertToAmountProducedPerSet(contrapNum,outputs[0],1);
                    for(int i=0;i<inputs.length;i++){
                        //backend.insertTo

                        backend.insertToProducedPerItem(contrapNum,inputs[i],Double.valueOf(amountPerItem[i]));
                        backend.insertToCurrStorages(contrapNum,inputs[i]);

                    }


*/


                }




            }

            backend.showContents();



            return this.backend;

        }catch (Exception e){
            System.out.println("Error With the File: "+e);
        }
        return this.backend;
    }
}
