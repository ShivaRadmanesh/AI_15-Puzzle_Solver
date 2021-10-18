public class Node implements Comparable {
   private int cost;
   private  int[][] state;
   private Node parent;
   private String id;
   private int depth;



    public Node(int[][] st){
       this.depth = 0;
       this.state = st;
       this.parent = null;
       this.id = this.toString();
       this.cost = this.depth;
    }

    public Node(Node p, int[][] st){
        this.depth = p.getDepth() + 1;
        this.state = st;
        this.parent = p;
        this.id = this.toString();
        this.cost = this.depth;
    }

    public Node(){
        this.depth = 0;
        this.state = new int[4][4];
        this.parent = null;
        this.id = this.toString();
    }

    public int getCost() {
        return cost;
    }

    public int[][] getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCost(int cost) {
        this.cost = cost + this.depth;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if( i == 3 && j == 3)
                    s += state[i][j];
                else
                 s += state[i][j] + ", ";
            }
        }

       return s;
    }

    @Override
    public int compareTo(Object o) {
        Node n = (Node)o;
        if(this.getCost() > n.getCost())
            return 1;
        if(this.getCost() < n.getCost())
            return -1;
        return 0;
    }
    @Override
    public boolean equals(Object o){
        Node n = (Node) o;
        if(this.getId().equals(n.getId()))
            return true;
        return false;
    }

}
