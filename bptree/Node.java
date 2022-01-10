import java.util.ArrayList;

public class Node {
    private class Pair{
        private int key;
        private int value;
        private Node leftChild;

        public Pair(int key, int value, Node node){
            this.key = key;
            this.value = value;
            this.leftChild = node;
        }
    }

    private ArrayList<Pair> p;  //key, value, left child
    private int m;      // # of keys
    private Node r;     // right most child

    public Node(){
        p = new ArrayList<>();
        m = 0;
        r = null;
    }

    public void addPair(int key, int value){
        p.add(new Pair(key,value,null));
        m++;
    }
    public void addPair(int key, int value, Node node){
        p.add(new Pair(key,value,node));
        m++;
    }
    public void addPair(int key, int value, int index){
        if(index==m) addPair(key, value);
        else{
            p.add(index, new Pair(key, value, null));
            m++;
        }
    }
    public void removePair(int index){
        this.p.remove(index);
        this.m--;
    }

    public void setLeftChild(int index, Node node){
        if(index==m) r = node;
        else p.get(index).leftChild = node;
    }
    public Node getLeftChild(int index){
        if(index==m) return this.r;
        return p.get(index).leftChild;
    }
    public void setR(Node r){
        this.r = r;
    }
    public Node getR(){
        return this.r;
    }
    public int getM() {
        return m;
    }
    public int getKey(int i) {
        return this.p.get(i).key;
    }
    public int getValue(int i){
        return this.p.get(i).value;
    }

    public Node findMostLeftLeaf(){
        Node tmpNode = this;
        while(tmpNode.getLeftChild(0)!=null) tmpNode = tmpNode.getLeftChild(0);
        return tmpNode;
    }
    public Node findMostRightLeaf(){
        Node tmpNode = this;
        while(tmpNode.getLeftChild(0)!=null) tmpNode = tmpNode.getR();
        return tmpNode;
    }
}
