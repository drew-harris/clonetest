package alexpetmecky.contraption.misc;

public class WeightedNode {
    private String source;
    private String target;
    private int weight;

    public WeightedNode(String source, String target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }


    public boolean equals(WeightedNode test){
        if((this.source ==test.getSource()) && (this.target == test.getTarget()) && (this.weight == test.getWeight())){
            return true;
        }else{
            return false;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
