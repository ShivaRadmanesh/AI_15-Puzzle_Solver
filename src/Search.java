import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Search {

    public static Node goal; //used for ida*

    ////////////////////////////////////////////
    // goalState method returns a node that is gaol (used for bds)
    public static Node goalState() {
        int[][] goalState = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
        Node goal = new Node(goalState);
        return goal;
    }

    ///////////////////////////////////////////
    //goal test function
    public static boolean goalTest(Node n) {
        String goal = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0";
        if (n.getId().equals(goal))
            return true;
        return false;
    }

    ///////////////////////////////////////////
    // successor function

    public static ArrayList successor(Node n) {
        int k = 0;
        int l = 0;
        int[][] tilesUp = new int[4][4];
        int[][] tilesDown = new int[4][4];
        int[][] tilesLeft = new int[4][4];
        int[][] tilesRight = new int[4][4];
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (n.getState()[i][j] == 0) {
                    k = i;
                    l = j;
                }
            }
        }
        if (k - 1 >= 0) { //up action

            for (int i = 0; i < 4; i++) { //copy the elements of the node state
                for (int j = 0; j < 4; j++) {
                    tilesUp[i][j] = n.getState()[i][j];
                }
            }


            int temp = tilesUp[k][l];
            tilesUp[k][l] = tilesUp[k - 1][l];
            tilesUp[k - 1][l] = temp;

            Node newNode = new Node(n, tilesUp);
            nodes.add(newNode);

        }

        if (k + 1 <= 3) { //down action
            for (int i = 0; i < 4; i++) { //copy the elements of the node state
                for (int j = 0; j < 4; j++) {
                    tilesDown[i][j] = n.getState()[i][j];
                }
            }

            int temp = tilesDown[k][l];
            tilesDown[k][l] = tilesDown[k + 1][l];
            tilesDown[k + 1][l] = temp;

            Node newNode = new Node(n, tilesDown);
            nodes.add(newNode);
        }

        if (l - 1 >= 0) { //left action
            for (int i = 0; i < 4; i++) { //copy the elements of the node state
                for (int j = 0; j < 4; j++) {
                    tilesLeft[i][j] = n.getState()[i][j];
                }
            }

            int temp = tilesLeft[k][l];
            tilesLeft[k][l] = tilesLeft[k][l - 1];
            tilesLeft[k][l - 1] = temp;

            Node newNode = new Node(n, tilesLeft);
            nodes.add(newNode);
        }

        if (l + 1 <= 3) { //right action
            for (int i = 0; i < 4; i++) { //copy the elements of the node's state
                for (int j = 0; j < 4; j++) {
                    tilesRight[i][j] = n.getState()[i][j];
                }
            }

            int temp = tilesRight[k][l];
            tilesRight[k][l] = tilesRight[k][l + 1];
            tilesRight[k][l + 1] = temp;

            Node newNode = new Node(n, tilesRight);
            nodes.add(newNode);
        }
        return nodes;
    }
    //////////////////////////////////////
    // solution function

    public static String solution(Node n) {
        String solution = "";
        ArrayList<Node> nodes = new ArrayList<>();
        Node current = new Node();
        current = n;
        while (current != null) {
            nodes.add(current);
            current = current.getParent();
        }
        for (int i = nodes.size() - 1; i >= 0; i--) {
            solution += nodes.get(i).getId() + "\n";
        }
        return solution;
    }

    /////////////////////////////////////////
    //backward solution function (for bidirectional search)
    public static String backwardSolution(Node n) {
        String bSolution = "";
        Node current = new Node();
        current = n;
        while (current != null) {
            bSolution += current.getId() + "\n";
            current = current.getParent();
        }
        return bSolution;
    }

    /////////////////////////////////////////
    //BFS
    public static String bfs(Node initState) {
        Node node = initState;
        if (goalTest(initState)) { //check if the node is already the goal
            return solution(initState);
        }
        Node child;
        ArrayList<Node> newStates;
        Queue<Node> frontier = new LinkedList<>();
        Set<Node> explored = new HashSet<>();
        frontier.add(node);
        while (!frontier.isEmpty()) {
            node = frontier.remove();
            explored.add(node);
            newStates = successor(node);
            for (int i = 0; i < newStates.size(); i++) {
                child = newStates.get(i);
                if (!frontier.contains(child) && !explored.contains(child)) {
                    if (goalTest(child))
                        return solution(child);
                    frontier.add(child);

                }
            }
        }


        return "failure";
    }

    ////////////////////////////////////////////////
    //UCS
    public static String ucs(Node initState) {
        Node node = initState;

        if (goalTest(initState)) { //check if the node is already the goal
            return solution(initState);
        }
        Node child;
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Set<Node> explored = new HashSet<>();
        ArrayList<Node> newStates;
        frontier.add(node);
        while (!frontier.isEmpty()) {
            node = frontier.remove();
            if (goalTest(node))
                return solution(node);
            explored.add(node);
            newStates = successor(node);
            for (int i = 0; i < newStates.size(); i++) {
                child = newStates.get(i);
                if (!explored.contains(child) && !frontier.contains(child))
                    frontier.add(child);
            }
        }
        return "failure";
    }
    ///////////////////////////////////////
    //dfs

    public static String dfs(Node initState) {
        Node node = initState;

        if (goalTest(initState)) { //check if the node is already the goal
            return solution(initState);
        }

        Node child;
        Node last;
        Node grandPar;
        Node current;
        Node toBeDeleted;
        ArrayList<Node> newStates;
        Stack<Node> frontier = new Stack<>();
        Set<Node> explored = new HashSet<>();
        frontier.push(node);
        last = null;

        while (!frontier.isEmpty()) {
            node = frontier.pop();
            last = node;

            if (node.getParent() != last) { //deleting extra nodes
                grandPar = node.getParent();
                current = last;
                while (current != grandPar) {
                    toBeDeleted = current;
                    current = current.getParent();
                    explored.remove(toBeDeleted);
                }
                System.gc();// when a node is not the child of the last expanded node, means that the last node
                //is a leaf of the search tree, and there is no pointer to
                // it(because each node has a pointer to its parent,Not its children!)
                //hence, when the garbage collector is called, the memory had been allocated for the last node would be released.
                //and when all of the children of a node are deleted, there
                //wouldn't be any pointer to to.so it would be deleted too.

            }

            explored.add(node);
            newStates = successor(node);
            for (int i = 0; i < newStates.size(); i++) {
                child = newStates.get(i);
                if (!frontier.contains(child) && !explored.contains(child)) {
                    if (goalTest(child))
                        return solution(child);
                    frontier.push(child);
                }
            }
        }


        return "failure";
    }

    ////////////////////////////////////////////////
    //bds
    public static String bds(Node initState) {
        Node forwardNode = initState;
        Node backwardNode = goalState();
        if (goalTest(initState)) { //check if the node is already the goal
            return solution(initState);
        }

        Node child;
        ArrayList<Node> newStates;
        Queue<Node> forwardFrontier = new LinkedList<>();
        Queue<Node> backwardFrontier = new LinkedList<>();
        Set<Node> forwardExplored = new HashSet<>();
        Set<Node> backwardExplored = new HashSet<>();
        forwardFrontier.add(forwardNode);
        backwardFrontier.add(backwardNode);
        Iterator<Node> itr;
        // Object[] bFrontier;
        Node current;


        while (!forwardFrontier.isEmpty() && !backwardFrontier.isEmpty()) {

            forwardNode = forwardFrontier.remove();
            forwardExplored.add(forwardNode);
            newStates = successor(forwardNode);
            for (int i = 0; i < newStates.size(); i++) { //updating forward frontier
                child = newStates.get(i);
                if (!forwardFrontier.contains(child) && !forwardExplored.contains(child))
                    forwardFrontier.add(child);
            }

            itr = backwardFrontier.iterator();
            // bFrontier = backwardFrontier.toArray();
            int index = -1;
            while (itr.hasNext()) { //checking if backward frontier and forward frontier have a common node
                //after updating forward frontier
                current = itr.next();
                for (int i = 0; i < newStates.size(); i++) {
                    if (current.equals(newStates.get(i)))
                        index = i;
                }
                if (index != -1) {
                    Node bCommon = newStates.get(index);
                    return (solution(bCommon) + backwardSolution(current.getParent()));
                }

            }


            backwardNode = backwardFrontier.remove();
            backwardExplored.add(backwardNode);
            newStates = successor(backwardNode);
            for (int i = 0; i < newStates.size(); i++) { //updating backward frontier
                child = newStates.get(i);
                if (!backwardFrontier.contains(child) && !backwardExplored.contains(child))
                    backwardFrontier.add(child);
            }

            index = -1;
            itr = forwardFrontier.iterator();
            //bFrontier = backwardFrontier.toArray();
            while (itr.hasNext()) { //checking if backward frontier and forward frontier have a common node
                //after updating backward frontier
                current = itr.next();
                for (int i = 0; i < newStates.size(); i++) {
                    if (current.equals(newStates.get(i)))
                        index = i;
                }
                if (index != -1) {
                    Node bCommon = newStates.get(index);
                    return (solution(current) + backwardSolution(bCommon.getParent()));
                }
            }


        }

        return "failure";
    }

    /////////////////////////////////////////
    //Manhattan distance heuristic function
    public static int manhattan(Node n) {
        int[][] tiles = n.getState();
        int x, y, value;
        int manhattanDistance = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                value = tiles[i][j];
                if (value != 0) {
                    x = (value - 1) / 4; // expected x coordinate
                    y = (value - 1) % 4; //expected y coordinate
                    manhattanDistance += Math.abs(x - i);
                    manhattanDistance += Math.abs(y - j);
                }
            }
        }
        return manhattanDistance;
    }

    /////////////////////////////////////////
    //Linear conflict heuristic
    public static int linearConflict(Node n) {
        int[][] tiles = n.getState();
        int x, y, value, expectedValue;
        int conflict = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                value = tiles[i][j];
                x = (value - 1) / 4; // expected x coordinate
                y = (value - 1) % 4; //expected y coordinate
                expectedValue = (i * 4) + j + 1;
                if (value != expectedValue) {
                    if (x == i) {// checking row for linear conflict
                        for (int k = j + 1; k < 4; k++) {
                            if (tiles[i][j] > tiles[i][k])
                                conflict += 2;
                        }
                    } else if (y == j) { //checking column for linear conflict
                        for (int k = j + 1; k < 4; k++) {
                            if (tiles[i][j] > tiles[k][j])
                                conflict += 2;
                        }
                    }
                }
            }
        }
        return conflict;
    }

    /////////////////////////////////////////
    //A*
    public static String aStar(Node initState) {
        Node node = initState;
        if (goalTest(node))   //check if the initial state is a goal state itself
            return solution(node);
        Node child;
        Node current;
        Node modifiable;
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Set<Node> explored = new HashSet<>();
        ArrayList<Node> newStates;
        frontier.add(node);
        Iterator<Node> itr;
        Object[] fArray;

        while (!frontier.isEmpty()) {
            node = frontier.remove();
            if (goalTest(node))
                return solution(node);
            explored.add(node);
            newStates = successor(node);
            for (int i = 0; i < newStates.size(); i++) {
                child = newStates.get(i);
                child.setCost(manhattan(child));
                if (!frontier.contains(child) && !explored.contains(child)) {
                    frontier.add(child);
                } else if (frontier.contains(child)) { //if the child state is in the frontier with the
                    //replace that frontier node with child
                    fArray = frontier.toArray();
                    for (int j = 0; j < fArray.length; j++) {
                        current = (Node) fArray[j];
                        if (child.equals(current)) {
                            if (child.compareTo(current) == -1) {
                                frontier.remove(current);
                                frontier.add(child);
                            }
                            break;
                        }
                    }
                }
            }
        }

        return "failure";
    }

    //////////////////////////////////////////
    //to1DArray (used for creating dataBase)
    public static int[] to1DArray(int[][] twoD) {
        int[] result = new int[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i * 4 + j] = twoD[i][j];
            }
        }
        return result;
    }

    //////////////////////////////////////////
    //creating dataBase
    public static void dataBase1() { //1, 2, 3, 4, 5

        int[][] state = new int[4][4];
        Node child;
        int[] arr = new int[5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0)
                    state[i][j] = j + 1;
                else state[i][j] = -1;
            }
        }
        state[1][0] = 5;
        state[3][3] = 0;


        int[] oneDArray;
//            oneDArray  = to1DArray(state);
//            for(int i = 0; i < 16; i ++){
//                if(oneDArray[i] == 1)
//                    arr[0] = i;
//                if(oneDArray[i] == 2)
//                    arr[1] = i;
//                if(oneDArray[i] == 3)
//                    arr[2] = i;
//                if(oneDArray[i] == 4)
//                    arr[3] = i;
//                if(oneDArray[i] == 5)
//                    arr[4] = i;
//            }

        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("data1.txt"), "utf-8"));

            Node node = new Node(state);
            ArrayList<Node> newStates;
            Queue<Node> frontier = new LinkedList<>();
            Set<Node> explored = new HashSet<>();
            frontier.add(node);
            while (!frontier.isEmpty()) {

                node = frontier.remove();
               // System.out.println(node.getState()[1]);
                oneDArray = to1DArray(node.getState());
                for (int k = 0; k < 16; k++) {
                    if (oneDArray[k] == 1)
                        arr[0] = k;
                    if (oneDArray[k] == 2)
                        arr[1] = k;
                    if (oneDArray[k] == 3)
                        arr[2] = k;
                    if (oneDArray[k] == 4)
                        arr[3] = k;
                    if (oneDArray[k] == 5)
                        arr[4] = k;
                }
                for (int n = 0; n < arr.length; n++) {
                      writer.write(arr[n] + " ");
                    System.out.print(arr[n] + " ");

                }
                writer.write(node.getCost() + "\n");
                System.out.print(node.getCost() + " \n");

                newStates = successor(node);
                for (int i = 0; i < newStates.size(); i++) {
                    if(!explored.contains(newStates.get(i)))
                    frontier.add(newStates.get(i));
                }

//                writer.write(node.getCost() + " ");
            }

        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }


    //////////////////////////////////////////
    public static void dataBase2(){ //6, 7, 8, 9, 10
        int[][] state = new int[4][4];
        Node child;
        int[] arr = new int[5];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                    state[i][j] = -1;
            }
        }
        state[1][1] = 6;
        state[1][2] = 7;
        state[1][3] = 8;
        state[3][3] = 0;
        state[2][0] = 9;
        state[2][1] = 10;


        int[] oneDArray ;
//            oneDArray  = to1DArray(state);
//            for(int i = 0; i < 16; i ++){
//                if(oneDArray[i] == 1)
//                    arr[0] = i;
//                if(oneDArray[i] == 2)
//                    arr[1] = i;
//                if(oneDArray[i] == 3)
//                    arr[2] = i;
//                if(oneDArray[i] == 4)
//                    arr[3] = i;
//                if(oneDArray[i] == 5)
//                    arr[4] = i;
//            }

        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("data2.txt"), "utf-8"));

            Node node = new Node(state);
            ArrayList<Node> newStates;
            Queue<Node> frontier = new LinkedList<>();
            Set<Node> explored = new HashSet<>();
            frontier.add(node);
            while (!frontier.isEmpty()) {

                node = frontier.remove();
                // System.out.println(node.getState()[1]);
                oneDArray = to1DArray(node.getState());
                for (int k = 0; k < 16; k++) {
                    if (oneDArray[k] == 6)
                        arr[0] = k;
                    if (oneDArray[k] == 7)
                        arr[1] = k;
                    if (oneDArray[k] == 8)
                        arr[2] = k;
                    if (oneDArray[k] == 9)
                        arr[3] = k;
                    if (oneDArray[k] == 10)
                        arr[4] = k;
                }
                for (int n = 0; n < arr.length; n++) {
                    writer.write(arr[n] + " ");
                    System.out.print(arr[n] + " ");

                }
                writer.write(node.getCost() + "\n");
                System.out.print(node.getCost() + " \n");

                newStates = successor(node);
                for (int i = 0; i < newStates.size(); i++) {
                    if(!explored.contains(newStates.get(i)))
                        frontier.add(newStates.get(i));
                }

//                writer.write(node.getCost() + " ");
            }

        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }
    //////////////////////////////////////////
    public static void dataBase3(){ //11, 12, 13, 14, 15
        int[][] state = new int[4][4];
        Node child;
        int[] arr = new int[5];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){

                state[i][j] = -1;
            }
        }
        state[3][0] = 13;
        state[3][1] = 14;
        state[3][2] = 15;
        state[3][3] = 0;
        state[2][2] = 11;
        state[2][3] = 12;


        int[] oneDArray ;
//            oneDArray  = to1DArray(state);
//            for(int i = 0; i < 16; i ++){
//                if(oneDArray[i] == 1)
//                    arr[0] = i;
//                if(oneDArray[i] == 2)
//                    arr[1] = i;
//                if(oneDArray[i] == 3)
//                    arr[2] = i;
//                if(oneDArray[i] == 4)
//                    arr[3] = i;
//                if(oneDArray[i] == 5)
//                    arr[4] = i;
//            }

        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("data3.txt"), "utf-8"));

            Node node = new Node(state);
            ArrayList<Node> newStates;
            Queue<Node> frontier = new LinkedList<>();
            Set<Node> explored = new HashSet<>();
            frontier.add(node);
            while (!frontier.isEmpty()) {

                node = frontier.remove();
                // System.out.println(node.getState()[1]);
                oneDArray = to1DArray(node.getState());
                for (int k = 0; k < 16; k++) {
                    if (oneDArray[k] == 11)
                        arr[0] = k;
                    if (oneDArray[k] == 12)
                        arr[1] = k;
                    if (oneDArray[k] == 13)
                        arr[2] = k;
                    if (oneDArray[k] == 14)
                        arr[3] = k;
                    if (oneDArray[k] == 15)
                        arr[4] = k;
                }
                for (int n = 0; n < arr.length; n++) {
                    writer.write(arr[n] + " ");
                    System.out.print(arr[n] + " ");

                }
                writer.write(node.getCost() + " \n");
                System.out.print(node.getCost() + "\n");

                newStates = successor(node);
                for (int i = 0; i < newStates.size(); i++) {
                    if(!explored.contains(newStates.get(i)))
                        frontier.add(newStates.get(i));
                }

//                writer.write(node.getCost() + " ");
            }

        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }

    }
    ///////////////////////////////////////////
    public Map<Integer, Integer> read1(){
        int[] arr = new int[5];
        int cost, hash;
        Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
        File file = new File("data1.txt");
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLong()){
                for(int i = 0; i < 5; i++){
                    arr[i] = scanner.nextInt();
                }
                cost = scanner.nextInt();
                hash = arr.hashCode();
                hm.put(hash, cost);

            }
        }catch(Exception e){}

        return hm;
    }
    //////////////////////////////////////////
    public Map<Integer, Integer> read2(){
        int[] arr = new int[5];
        int cost, hash;
        Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
        File file = new File("data2.txt");
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLong()){
                for(int i = 0; i < 5; i++){
                    arr[i] = scanner.nextInt();
                }
                cost = scanner.nextInt();
                hash = arr.hashCode();
                hm.put(hash, cost);

            }
        }catch(Exception e){}

        return hm;
    }
    //////////////////////////////////////////
    public Map<Integer, Integer> read3(){
        int[] arr = new int[5];
        int cost, hash;
        Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
        File file = new File("data3.txt");
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLong()){
                for(int i = 0; i < 5; i++){
                    arr[i] = scanner.nextInt();
                }
                cost = scanner.nextInt();
                hash = arr.hashCode();
                hm.put(hash, cost);

            }
        }catch(Exception e){}

        return hm;
    }
    //////////////////////////////////////////
    //ida*
    public static String idaStar(Node initState){
            int bound = manhattan(initState) + linearConflict(initState);
            Stack<Node> frontier = new Stack<>();
            frontier.push(initState);
            int t;
            while(true){
                t = search(frontier, 0, bound);
                if(t == -1)
                    return solution(goal);
                if(t == Integer.MAX_VALUE)
                    return "failure";
                bound = t;
            }
    }
    //search
    public static int search(Stack<Node> frontier, int g, int bound){
            ArrayList<Node> newStates;
            Node child;
            Node node = frontier.peek();
            int f = g + manhattan(node) + linearConflict(node);
            if(f > bound) return f;
            if(goalTest(node)) {
                goal = node;
                return -1;
            }
                int min = Integer.MAX_VALUE;
                newStates = successor(node);
                for (int i = 0; i < newStates.size(); i++){
                    child = newStates.get(i);
                    if(!frontier.contains(child)){
                        frontier.push(child);
                        int t = search(frontier,g + node.getDepth(), bound);
                        if(t == -1)
                            return -1;
                        if(t < min)
                            min = t;
                        frontier.pop();
                    }
                }
                return min;

    }



    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        int[][] test= new int[4][4];
        int[][] testc= new int[4][4];
        int[][] sample = new int[4][4];
        {


            sample[0][0] = 1;
            sample[0][1] = 2;
            sample[0][2] = 3;
            sample[0][3] = 4;

            sample[1][0] = 5;
            sample[1][1] = 6;
            sample[1][2] = 7;
            sample[1][3] = 8;

            sample[2][0] = 9;
            sample[2][1] = 10;
            sample[2][2] = 11;
            sample[2][3] = 12;

            sample[3][0] = 13;
            sample[3][1] = 14;
            sample[3][2] = 0;
            sample[3][3] = 15;
        }
        String s;
        ArrayList<Node> nodes = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                test[i][j] = (i + 1) * (j + 1);
            }
        }
        int k = 1;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                testc[i][j] =  k;
                k++;
            }
        }
        testc[1][2]= 0;
        Node par = new Node(test);
        Node child = new Node(sample);
        Node khar = goalState();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                sample[i][j] = sc.nextInt();
            }

        }
//        System.out.println(child.toString());
//       // System.out.println(child.getCost());
//        System.out.println(goalTest(child));
//        nodes = successor(child);
//        for(int i = 0; i < nodes.size(); i++){
//            System.out.println(nodes.get(i));
//        }
//        System.out.println(child.toString());
//        System.out.println(goalTest(child));

        s = idaStar(child);
        System.out.println(s);


     //   dataBase1();
    }
}
