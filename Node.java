/*
 * CSCI311A: Artificial Intelligence
 * Milka Murdjeva
 * Node.java
 * 
 */

import java.util.*;

/**
 * Represents a Node for the n-Puzzle 
 */
public class Node implements Comparable{
    
    private static int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}}; //the goal configuration of the board.
    
    //Properties of this Node
    private int size = 3; //3 is the board size for 8-Puzzle.
    int[][] board;
    private Node parent;
    private int depth; //depth of this node
    
    
    //Added boards for my heuristic function
    //private static int[][] board1 = {{1, 2, 3}, {4, 5, 6}, {0, 7, 8}};
    //private static int[][] board2 = {{1, 2, 3}, {4, 0, 6}, {7, 5, 8}};
    //private static int[][] board3 = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
    //private static int[][] board4 = {{1, 2, 0}, {4, 5, 3}, {7, 8, 6}};

   
    /**
     * Constructor to be used by BFS search.
     * par - The parent node.
     * dep - The depth of this node.
     * brd - The board - a 3x3 array of integers from 0 to 15.
     *              The 0 is the blank spot.     
     */
    public Node(Node par, int dep, int[][] brd) {
        
        
        this.parent = par;
        this.depth = dep;
        this.board = (int[][]) brd.clone();
        
    }
     
    /*************
     ********** Setter and Getter methods for Node variables **********
     *************/
    public void setparent(Node par)
    {
        this.parent = par;
    }
    
    public Node getparent()
    {
        return this.parent;
    }

    public void setdepth(int dep)
    {
        this.depth = dep;
    }
    
    public int getdepth()
    {
        return this.depth;
    }

    public void setboard(int[][] brd)
    {
        this.board = brd;
    }
    
    public int[][] getboard()
    {
        return this.board;
    }
    
    
    /*************
     ********** End of Setter and Getter methods **********
     *************/
    
    /**
     * Returns true if the state of this node is the goal state of the puzzle.
     */
    public boolean isGoal() {
    
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
                if(board[i][j] != goal[i][j])
                    return false;
        }
            
        return true;
    }
    
         
    
    /**
     * Returns true if brd is same as this board.
     */
    public boolean isSameBoard(int[][] brd) {
        
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
                if(this.board[i][j] != brd[i][j])
                    return false;
        }
            
        return true;
    }
    
    /**
     * TO DO
     * Expands the current board to create the new states. 
     * The next possible states are based on the current location of the '0' (ie. blank spot)
     */
    public ArrayList<int[][]> expand()
    {    
        ArrayList<int[][]> nodeslist = new ArrayList<int[][]>();
        
        //If the '0' (blank spot) is at board[0][0], we can either move the blank 
        //down or to the right.
        if(board[0][0] == 0){
            nodeslist.add(moveBlankDown(0, 0));
            nodeslist.add(moveBlankRight(0, 0));
            //System.out.println("case 1");
        }
        //Else if the blank spot is at board[0][1], we can move the blank
        //down, right, left.
        else if(board[0][1] == 0){
            nodeslist.add(moveBlankDown(0, 1));
            nodeslist.add(moveBlankRight(0, 1));
            nodeslist.add(moveBlankLeft(0, 1));
        }
        //If the blank spot is at board[0][2], we can move the blank either down
        //or left
        else if(board[0][2] == 0){
            nodeslist.add(moveBlankDown(0, 2));
            nodeslist.add(moveBlankLeft(0, 2));
        }
        //Etc.. for all the possible spots of the blank (edge, center, side)
        else if(board[1][0] == 0){
            nodeslist.add(moveBlankDown(1, 0));
            nodeslist.add(moveBlankRight(1, 0));
            nodeslist.add(moveBlankUp(1, 0));
        }
        else if(board[1][1] == 0){
            nodeslist.add(moveBlankDown(1, 1));
            nodeslist.add(moveBlankRight(1, 1));
            nodeslist.add(moveBlankLeft(1, 1));
            nodeslist.add(moveBlankUp(1, 1));
        }
        else if(board[1][2] == 0){
            nodeslist.add(moveBlankDown(1, 2));
            nodeslist.add(moveBlankUp(1, 2));
            nodeslist.add(moveBlankLeft(1, 2));
        }
        else if(board[2][0] == 0){
            nodeslist.add(moveBlankUp(2, 0));
            nodeslist.add(moveBlankRight(2, 0));
        }
        else if(board[2][1] == 0){
            nodeslist.add(moveBlankUp(2, 1));
            nodeslist.add(moveBlankRight(2, 1));
            nodeslist.add(moveBlankLeft(2, 1));
        }
        else if(board[2][2] == 0){
            nodeslist.add(moveBlankUp(2, 2));
            nodeslist.add(moveBlankLeft(2, 2));
        }
        
        return nodeslist;
    }
    
    
    
    /**
     * Moves the blank down by swapping the '0' with the entry below it
     */
    public int[][] moveBlankDown(int row, int col)
    {
        int[][] newboard = new int[size][size];
        newboard = copyBoard();
        
        if(row+1<size)
            swap(newboard, row, col, row+1, col);
        else
            System.out.println("row out of bounds in moveBlankDown: "+row);

        
        return newboard;
    }
    
    
    /**
     * Moves the blank up by swapping the '0' with the entry above it
     */
    public int[][] moveBlankUp(int row, int col)
    {
        int[][] newboard = new int[size][size];
        newboard = copyBoard();
        
        if(row-1>=0)
            swap(newboard, row, col, row-1, col);
        else
            System.out.println("row out of bounds in moveBlankUp: "+row);
        
        return newboard;
    }
    
    
    
    /**
     * Moves the blank right by swapping the '0' with the entry right of it
     */
    public int[][] moveBlankRight(int row, int col)
    {
        int[][] newboard = new int[size][size];
        newboard = copyBoard();
        
        if(col+1<size)
            swap(newboard, row, col, row, col+1);
        else
            System.out.println("col out of bounds in moveBlankRight: "+col);
        
    
        
        return newboard;
    }
    
    
    /**
     * Moves the blank left by swapping the '0' with the entry left of it
     */
    public int[][] moveBlankLeft(int row, int col)
    {
        int[][] newboard = new int[size][size];
        newboard = copyBoard();
        
        if(col-1>=0)
            swap(newboard, row, col, row, col-1);
        else
            System.out.println("row out of bounds in moveBlankLeft: "+col);
        
        
        return newboard;
    }
    
    
    /*
     * Prints the board configuration of the given Node
     */
    public void print(Node node)
    {
        int[][] brd = node.getboard();
        
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++){
                if(brd[i][j]==0){
                    System.out.print("  ");
                    continue;
                }
                if (brd[i][j] <10)
                    System.out.print(" ");
                System.out.print(brd[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    

    /*
     * Method to determine if two states are equal, where a state is a node or a board.
     * Parameter o can either be a Node or an int[][] (i.e. the board)
     */
    public boolean equals(Object o)
    {
        //if the object to compare is a Node
        if(o.getClass() == this.getClass()){
            
            if(this.toString().equals( ((Node)o).toString()))
                   return true;
              else
                  return false;
        }
        //if the object to compare is an int[][]
        else if(o.getClass() == this.board.getClass()){
            if(isSameBoard((int[][]) o))
                return true;
            else
                return false;
        }
        //if the object to compare is something weird (code shouldn't come here!)
        else{
            System.out.println("something weird");
            return false;
        }
    }
    
    /**
     * Returns the String representation of this node's state.
     */
    public String toString() {
        String sb = "";
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
               
                if (board[i][j] == 0) {
                    sb += "  ";
                    continue;
                }
                if (board[i][j] < 10) {
                    sb += " ";
                }
                sb+=board[i][j];
            }
            sb+="\n";
        }
        return sb;
    }
    
    
    /****Private Helper Methods****/
    
    /**
     * Helper method for the moveBlank methods
     */
    private int[][] copyBoard()
    {
        int[][] newboard = new int[size][size];
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++)
                newboard[i][j] = board[i][j];
        }
        
        return newboard;
    }

    
    
    /**
     * Helper method for the moveBlank methods
     */
    private void swap(int[][] node, int x1, int y1, int x2, int y2)
    {
        int tmp = node[x1][y1];
        node[x1][y1] = node[x2][y2];
        node[x2][y2] = tmp;
        
    }
    
    
    //------------------A* Code Begins Here-------------------------//
    
    private double gvalue; //To be used by A*Star search
    private double hvalue; //To be used by A*Star search
    
    
    /**
     * Constructor to be used by A*Star search only.
     * par - The parent node.
     * gval - The g-value for this node.
     * hval - The h-value for this node.
     * brd - The board which should be a 3x3 array of integers from 0 to 8. 
     */
    public Node(Node par, double gval, double hval, int[][] brd) {
        
        
        this.parent = par;
        this.gvalue = gval;
        this.hvalue = hval;
        this.board = (int[][]) brd.clone();
        
    }


    
    /*************
     ********** Setter and Getter methods for A* Search variables **********
     *************/
    public void setgvalue(double g)
    {
        this.gvalue = g;
    }
    
    public double getgvalue()
    {
        return this.gvalue;
    }

    public void sethvalue(double h)
    {
        this.hvalue = h;
    }
    
    public double gethvalue()
    {
        return this.hvalue;
    }
    
    
    /**
     * Used by A* Search only.
     * Returns the heuristic value. The heuristic for the state of this node is the sum of Manhattan
     * distances from each tile's position to that tile's final position.
     */
   public double evaluateHeuristic() {
       int distance = 0;
       int[] location = new int[2];
       
       //check all tiles
       for (int i = 0; i < size; i++){
           for (int j = 0; j < size; j++){
               int tile = board[i][j];
               //if tile is not a blank
               //find next tile and update distance
               if (tile != 0){
                   location[0] = (tile - 1)/size;
                   location[1] = (tile - 1)%size;
                   distance += getManhattanDistance(i, j, location[0], location[1]);
               }
               //if tile is blank update last distance
               if (tile == 0){
                   distance += getManhattanDistance(i, j, size-1, size-1);
               }
           }
       }

       return distance;
    }
   
   /* 
    * We created a new function for our heuristic which uses the misplaced
    * tiles heuristic with an utilization of the Manhattan Distance heuristic.
    */
   public double evaluateMyHeuristic(){
       int distance = 0;
       int[] blank = new int[2];//keep the location of the blank tile
       int misplaced = 0;
       
       //check board to find location of blank tile
       for (int i = 0; i < size; i++){
           for (int j = 0; j < size; j++){
               if (board[i][j] == 0){ 
                   blank[0] = i;
                   blank[1] = j;
               }
               //if the tile is not in it's right location increase num of misplaced
               else if (board[i][j] != i*3 + j + 1){
                   misplaced ++;
               }
           }
       }
       //calculate number of misplaced tiles plus the Manhattan Distance for the blank tile
       distance += (misplaced + getManhattanDistance(size-1, size-1, blank[0], blank[1])/(size*size));
       
       return distance;
   }
   
   public int compareTo(Object other){
       if ((this.gethvalue() + this.getgvalue()) > (((Node) other).gethvalue() + ((Node) other).getgvalue())){
           return 1;
       }
       else{
           return 0;
       }
   }
    
    
    
    /**
     * Helper method used by A* Search only. 
     * Returns the Manhattan distance between the given two cells of the 4x4 board.
     */
    private static int getManhattanDistance(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2);
    }
 
}