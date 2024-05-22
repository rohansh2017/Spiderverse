package spiderman;

import java.util.*;



/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 * i. a (int): number of dimensions in the graph
 * ii. b (int): the initial size of the cluster table prior to rehashing
 * iii. c (double): the capacity(threshold) used to rehash the cluster table
 * 2. a lines, each with:
 * i. The dimension number (int)
 * ii. The number of canon events for the dimension (int)
 * iii. The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 * i. The dimension they are currently at (int)
 * ii. The name of the person (String)
 * iii. The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 * i. The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 * is at the same Dimension (if one exists, space separated) followed by
 * the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    static Clusters cs;
    static Collider c = new Collider();
    private int[] edgeTo;
    private boolean[] marked;

    public void bfs(Person p, ArrayList<DimensionNode> adjList, DimensionNode source, boolean hasSpider) {
        edgeTo = new int[adjList.size()];
        marked = new boolean[adjList.size()];

        for (int i = 0; i < adjList.size(); i++)
            marked[i] = false;

        int currentDim = p.getCurrentDimension();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(source.getDimension().getDimensionNumber());
        marked[adjList.indexOf(source)] = true;
        int dim;
        while (!queue.isEmpty()) {
            dim = queue.poll();
            if (dim == currentDim) {
                break;
            }

            for (DimensionNode ptr = adjList.get(adjList.indexOf(c.getDimensionNode(dim))); ptr != null; ptr = ptr.getNext()) {
                if (!marked[adjList.indexOf(c.getDimensionNode(ptr.getDimension().getDimensionNumber()))]) {
                    queue.add(ptr.getDimension().getDimensionNumber());
                    marked[adjList.indexOf(c.getDimensionNode(ptr.getDimension().getDimensionNumber()))] = true;
                    edgeTo[adjList.indexOf(c.getDimensionNode(ptr.getDimension().getDimensionNumber()))] = dim;
                }
            }
            
        }
        int ptr = p.getCurrentDimension();
        Stack<Integer> s = new Stack<Integer>();
            while (ptr != source.getDimension().getDimensionNumber()){
                
                s.add(ptr);
                ptr = edgeTo[adjList.indexOf(c.getDimensionNode(ptr))];
            }
        s.add(source.getDimension().getDimensionNumber());
        
        if (hasSpider){
            Stack<Integer> temp = new Stack<Integer>();
            int size = s.size();
            for (int i = 0; i < size; i++){
                temp.push(s.pop());
            }
            while (!temp.isEmpty()){
                StdOut.print(temp.pop() + " ");
            }
        } else {
            Stack<Integer> temp = new Stack<Integer>();
            while (s.size() != 1){
                temp.add(s.peek());
                StdOut.print(s.pop() + " ");
            }
            StdOut.print(s.pop() + " ");
            while (!temp.isEmpty()){
                StdOut.print(temp.pop() + " ");
            }
        }
        p.setCurrentDimension(source.getDimension().getDimensionNumber());
    }

    public static void main(String[] args) {

        if (args.length < 4) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
            return;
        }
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String outputFileName = args[3];

        cs = new Clusters();
        cs.readFile(dimensionInputFile);
        DimensionNode[] clusters = cs.getClusters();

        c = new Collider();
        c.makeAdjList(clusters);
        ArrayList<DimensionNode> adjList = c.getAdjList();
        StdIn.setFile(spiderverseInputFile);
        c.insertPeople();
        Person[] people = c.getPeople();

        StdIn.setFile(hubInputFile);
        int hub = StdIn.readInt();

        DimensionNode dn = c.getDimensionNode(hub);

        CollectAnomalies ca = new CollectAnomalies();
        StdOut.setFile(outputFileName);
        for (Person p : people) {
            boolean hasSpider = false;
            if (p.getCurrentDimension() != hub && p.getCurrentDimension() != p.getDimensionalSignature()) {
                StdOut.print(p.getName() + " ");
                for (int i = 0; i < people.length; i++) {
                    if (people[i].getCurrentDimension() == people[i].getDimensionalSignature()
                            && people[i].getCurrentDimension() == p.getCurrentDimension()) {
                        StdOut.print(people[i].getName() + " ");
                        hasSpider = true;
                        break;
                    }
                }
                ca.bfs(p, adjList, dn, hasSpider);
                StdOut.println();
                
            }
        }

    }
}
