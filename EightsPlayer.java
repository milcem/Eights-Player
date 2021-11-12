/*
 * CSCI311A: Artificial Intelligence
 * Milka Murdjeva
 * EightsPlayer.java
 * 
 */

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * Solves the 8-Puzzle Game (can be generalized to n-Puzzle)
 */

public class EightsPlayer {

    static Scanner scan = new Scanner(System.in);
    static int size=3; //size=3 for 8-Puzzle
    static int numiterations = 100;
    static int numnodes; //number of nodes generated
    static int nummoves; //number of moves required to reach goal
    
    
    public static void main(String[] args)
    {    
        int boardchoice = getBoardChoice();
        int algchoice = getAlgChoice();
            
        int numsolutions = 0;
        
        Node initNode;

        if(boardchoice==0)
            numiterations = 1;

        for(int i=0; i<numiterations; i++){
        
            if(boardchoice==0)
                initNode = getUserBoard();
            else
                initNode = generateInitialState();//create the random board for a new puzzle
            
            boolean result=false; //whether the algorithm returns a solution
            
            switch (algchoice){
                case 0: 
                    result = runBFS(initNode); //BFS
                    break;
                case 1: 
                    result = runAStar(initNode, 0); //A* with Manhattan Distance heuristic
                    break;
                case 2: 
                    result = runAStar(initNode, 1); //A* with your new heuristic
                    break;
            }
            
            
            //if the search returns a solution
            if(result){
                
                numsolutions++;
                
                
                System.out.println("Number of nodes generated to solve: " + numnodes);
                System.out.println("Number of moves to solve: " + nummoves);            
                System.out.println("Number of solutions so far: " + numsolutions);
                System.out.println("_______");        
                
            }
            else
                System.out.print(".");
            
        }//for

        
        
        System.out.println();
        System.out.println("Number of iterations: " +numiterations);
        
        if(numsolutions > 0){
            System.out.println("Average number of moves for "+numsolutions+" solutions: "+nummoves/numsolutions);
            System.out.println("Average number of nodes generated for "+numsolutions+" solutions: "+numnodes/numsolutions);
        }
        else
            System.out.println("No solutions in "+numiterations+" iterations.");
        
    }
    
    
    public static int getBoardChoice()
    {
        
        System.out.println("single(0) or multiple boards(1)");
        int choice = Integer.parseInt(scan.nextLine());
        
        return choice;
    }
    
    public static int getAlgChoice()
    {
        
        System.out.println("BFS(0) or A* Manhattan Distance(1) or A* Blank Moves(2)");
        int choice = Integer.parseInt(scan.nextLine());
        
        return choice;
    }

    
    public static Node getUserBoard()
    {
        
        System.out.println("Enter board: ex. 012345678");
        String stbd = scan.nextLine();
        
        int[][] board = new int[size][size];
        
        int k=0;
        
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                //System.out.println(stbd.charAt(k));
                board[i][j]= Integer.parseInt(stbd.substring(k, k+1));
                k++;
            }
        }
        
        
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                //System.out.println(board[i][j]);
            }
            System.out.println();
        }
        
        
        Node newNode = new Node(null,0, board);

        return newNode;
        
        
    }

    
    
    
    /**
     * Generates a new Node with the initial board
     */
    public static Node generateInitialState()
    {
        int[][] board = getNewBoard();
        
        Node newNode = new Node(null,0, board);

        return newNode;
    }
    
    
    /**
     * Creates a randomly filled board with numbers from 0 to 8. 
     * The '0' represents the empty tile.
     */
    public static int[][] getNewBoard()
    {
        
        int[][] brd = new int[size][size];
        Random gen = new Random();
        int[] generated = new int[size*size];
        for(int i=0; i<generated.length; i++)
            generated[i] = -1;
        
        int count = 0;
        
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                int num = gen.nextInt(size*size);
                
                while(contains(generated, num)){
                    num = gen.nextInt(size*size);
                }
                
                generated[count] = num;
                count++;
                brd[i][j] = num;
            }
        }
        
        /*
        //Case 1: 12 moves
        brd[0][0] = 1;
        brd[0][1] = 3;
        brd[0][2] = 8;
        
        brd[1][0] = 7;
        brd[1][1] = 4;
        brd[1][2] = 2;
        
        brd[2][0] = 0;
        brd[2][1] = 6;
        brd[2][2] = 5;
        */
        
        return brd;
        
    }
    
    /**
     * Helper method for getNewBoard()
     */
    public static boolean contains(int[] array, int x)
    { 
        int i=0;
        while(i < array.length){
            if(array[i]==x)
                return true;
            i++;
        }
        return false;
    }
    
    
    public static void printSolution(Node node) {
        
    	//we get the string configuration of the current board(node)
        String board_config = node.toString();
        //and use a trace node to check the path we found to a solution
        Node trace = node;
        //print out all node states that we find which are all the moves to the solution
        nummoves = 0;
        
        while (trace.getdepth() > 1){
            trace = trace.getparent();
            nummoves ++; 
            board_config = trace.toString() + "\n" + board_config;
        }
        nummoves++;
        board_config = trace.getparent().toString() + "\n" + board_config;
        System.out.println(board_config);
        

    }
    
    
    public static boolean runBFS(Node initNode)
    {
        Queue<Node> Frontier = new LinkedList<Node>(); //use a queue for the frontier
        ArrayList<Node> Explored = new ArrayList<Node>(); //use an array for explored nodes
        int maxDepth = 13;

        Frontier.add(initNode);  //add initial node to the frontier

        //while the frontier is not empty
        while(!Frontier.isEmpty()){
            Node node = Frontier.poll(); //remove last node from frontier
            if (node.isGoal()){ //check if node is goal
                printSolution(node);
                return true;
            } // if node is not goal:
            ArrayList<int [][]> nodeslist = node.expand();
            for (int i = 0; i< nodeslist.size(); i++){
                Node new_node = new Node(node,node.getdepth()+1, nodeslist.get(i));
                if (new_node.getdepth() >= maxDepth){ //check if nodes generated are more
                    return false;					  //than maxDepth and terminate if so
                }
                //if explored and frontier don't contain the node
                //add to frontier and increase number of nodes (for count)
                if (!Explored.contains(new_node) && !Frontier.contains(new_node)){
                    Frontier.add(new_node);
                    numnodes ++;
                }
            }
            //add node to explored
            Explored.add(node);
        }
        
        return true;
          
    }
    
    
    
    /***************************A* Code Starts Here ***************************/
    
    /**
     * TO DO:
     * Runs A* Search to find the goal state.
     * Return true if a solution is found; otherwise returns false.
     * heuristic = 0 for Manhattan Distance, heuristic = 1 for your new heuristic
     */
    public static boolean runAStar(Node initNode, int heuristic)
    {
        PriorityQueue<Node> Frontier = new PriorityQueue<Node>(); //frontier pq
        ArrayList<Node> Explored = new ArrayList<Node>(); //explored list
        int maxDepth = 13;
        Node node;
        int gen_nodes = 0;

        initNode.setgvalue(0); //set g-value to initial node to 0
        Frontier.add(initNode); //add initial node to frontier

        	//while the frontier is not empty
            while(!Frontier.isEmpty()){
                node = Frontier.poll(); //look at last node on frontier
                if (node.isGoal()){ //if node is goal print solution
                    printSolution(node);
                    numnodes += gen_nodes;
                    return true;
                }
                
                //expanding node and putting resulting nodes in array
                ArrayList<int [][]> nodeslist = node.expand();
                
                //check all nodes and make new nodes with evaluated heuristic 
                for (int i = 0; i< nodeslist.size(); i++){
                    Node new_node = new Node(node,(int) node.getgvalue()+1, nodeslist.get(i));
                    
                    //check which heuristic was chosen:
                    //[0] Manhattan Distance [1] Our Heuristic
                    if (heuristic == 0){
                        new_node.sethvalue(new_node.evaluateHeuristic());
                    }
                    else{
                        new_node.sethvalue(new_node.evaluateMyHeuristic());
                    }
                    
                    //set g values for nodes, if it is more than maxDepth return false
                    new_node.setgvalue((int) new_node.getdepth());
                    if (new_node.getdepth() >= maxDepth){
                        return false;
                    }
                    //if explored and frontier don't contain node, add to frontier
                    if (!Explored.contains(new_node) && !Frontier.contains(new_node)){
                        Frontier.add(new_node);
                        gen_nodes ++;
                    }
                }
                //and finally add node to explored
                Explored.add(node);
                
            }
            
        return true;
    }
    
}
