package alexpetmecky.contraption.misc;

public class NodeMeta {
    private String source;
    private String target;
    private int weight;

    private NodeMeta parent;
    private boolean isRecipeNode; //as opposed to an item node

    public NodeMeta(String source, String target, int weight, boolean isRecipeNode, NodeMeta parent) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.isRecipeNode = isRecipeNode;
        this.parent = parent;
    }

    public NodeMeta(WeightedNode node,boolean isRecipeNode,NodeMeta parent){
        this.source = node.getSource();
        this.target = node.getTarget();
        this.weight = node.getWeight();
        this.isRecipeNode = isRecipeNode;
        this.parent = parent;
    }

    public boolean equals(NodeMeta test){
        if( (this.source ==test.getSource()) && (this.target == test.getTarget()) && (this.weight == test.getWeight()) ){
            return true;
        }else{
            return false;
        }

        //return true;
    }


    public WeightedNode convertToWeighted(){
        WeightedNode retVal = new WeightedNode(this.source,this.target,this.weight);
        return retVal;
    }

    public String condenseString(){
        return source+"|"+target+"|"+weight;
    }

    public String toString(){
        return source+"|"+target+"|"+weight;
        //return "Source: "+source+"\n"+"Target: "+target+"\n"+"Weight: "+weight+"\n";
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

    public NodeMeta getParent() {
        return parent;
    }

    public void setParent(NodeMeta parent) {
        this.parent = parent;
    }

    public boolean isRecipeNode() {
        return isRecipeNode;
    }

    public void setRecipeNode(boolean recipeNode) {
        isRecipeNode = recipeNode;
    }
}
