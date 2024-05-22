package spiderman;


import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {

    boolean[] marked;
    ArrayList<Integer> dfsPath;

    public void dfs(DimensionNode source, ArrayList<DimensionNode> adjList, int endDim){
        marked = new boolean[adjList.size()];
        dfsPath = new ArrayList<Integer>();
        dfsRecursive(source, adjList, endDim);
    }

    public void dfsRecursive(DimensionNode source, ArrayList<DimensionNode> adjList, int endDim){
        if (marked[adjList.indexOf(getNode(endDim, adjList))])
            return;

       if (source.getDimension().getDimensionNumber() == endDim){
        dfsPath.add(endDim);
        return;
       }
       
       dfsPath.add(source.getDimension().getDimensionNumber());
       DimensionNode ptr = adjList.get(findIndex(source.getDimension().getDimensionNumber(), adjList)).getNext();
       marked[adjList.indexOf(source)] = true;

       while (ptr != null){
        
        if (!marked[adjList.indexOf(ptr)]){
            dfsRecursive(ptr, adjList, endDim);
        }
        ptr = ptr.getNext();

        }
    }

    public DimensionNode getNode(int dimNum, ArrayList<DimensionNode> graph){
        for (int i = 0; i < graph.size(); i++){
            DimensionNode ptr;
            for (ptr = graph.get(i); ptr != null; ptr = ptr.getNext()){
                if (ptr.getDimension().getDimensionNumber() == dimNum) return ptr;
            }
            
        }
        return null; 
    }

    public int findIndex(int  dimNum, ArrayList<DimensionNode> graph){
        for (int i = 0; i < graph.size(); i++){
            if (graph.get(i).getDimension().getDimensionNumber() == dimNum)
                return i;
        }
        return -1;
    }

    

    
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        String dimensionInputFile =  args[0];
        String spotInputFile = args[2];
        String trackspotoutputfile = args[3];


       Clusters cs = new Clusters();
       Collider c = new Collider();
       TrackSpot ts = new TrackSpot();
       
       cs.readFile(dimensionInputFile);
       c.makeAdjList(cs.getClusters());
       ArrayList<DimensionNode> adjList = c.getAdjList();

       StdIn.setFile(spotInputFile);
       StdOut.setFile(trackspotoutputfile);
       int hub = StdIn.readInt();
       int end = StdIn.readInt();
       ts.dfs(ts.getNode(hub, adjList), adjList, end);

       for (int i = 0; i < ts.dfsPath.size(); i++){
        if (ts.dfsPath.get(i) != end){
            StdOut.print(ts.dfsPath.get(i) + " ");
        } else if (ts.dfsPath.get(i) == end){
            StdOut.print(ts.dfsPath.get(i));
            break;
        }
       }
    }
}
