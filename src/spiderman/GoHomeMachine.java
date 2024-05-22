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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {
    
    

    public void goHome(Person p, int time, ArrayList<DimensionNode> adjList, DimensionNode source){
        List<DimensionNode> done = new ArrayList<DimensionNode>();
        List<DimensionNode> fringe = new ArrayList<DimensionNode>();
        List<Integer> d = new ArrayList<Integer>();
        List<DimensionNode> pred = new ArrayList<DimensionNode>();

        for (int i = 0; i < adjList.size(); i++){
            d.add(-1);
            pred.add(null);
        }

        d.set(adjList.indexOf(source), 0);
        pred.set(adjList.indexOf(source), null);

        fringe.add(adjList.get(adjList.indexOf(source)));

        while (!fringe.isEmpty()){
            DimensionNode m = findMin(fringe, d, adjList);
            done.add(adjList.get(adjList.indexOf(m)));
            
            for (DimensionNode ptr = adjList.get(adjList.indexOf(m)); ptr != null; ptr = ptr.getNext()){
                if (!done.contains(adjList.get(adjList.indexOf(ptr)))){
                    
                    int distance = d.get(adjList.indexOf(m)) + ptr.getDimension().getWeight() + m.getDimension().getWeight(); // look at w(m,w);
    
                    if(d.get(adjList.indexOf(ptr)) == -1){
                        d.set(adjList.indexOf(ptr), distance);
                        fringe.add(adjList.get(adjList.indexOf(ptr)));
                        pred.set(adjList.indexOf(ptr), m);
                    } else if (d.get(adjList.indexOf(ptr)) > distance){
                        d.set(adjList.indexOf(ptr), distance);
                        pred.set(adjList.indexOf(ptr), m);
                    }
                }
            }
            
        }
        
        Stack<Integer> s = new Stack<Integer>();
        
        int dimensionalSignature = p.dimensionalSignature;
        while (dimensionalSignature != source.getDimension().getDimensionNumber()){
            s.add(dimensionalSignature);
            dimensionalSignature = pred.get(adjList.indexOf(getNode(adjList, dimensionalSignature))).getDimension().getDimensionNumber();
        }
        boolean check = true;
        if (d.get(adjList.indexOf(getNode(adjList, p.getDimensionalSignature()))) > time){
            getNode(adjList, p.getDimensionalSignature()).getDimension().setCanonNumber(getNode(adjList, p.getDimensionalSignature()).getDimension().getCanonNumber()-1);
            check = false;
        }
        StdOut.print(getNode(adjList, p.getDimensionalSignature()).getDimension().getCanonNumber() + " ");
        s.add(source.getDimension().getDimensionNumber());
        StdOut.print(p.getName() + " ");
        if (check){
            StdOut.print("SUCCESS " );
        } else {
            StdOut.print("FAILED ");
        }
        while (!s.isEmpty()){
            //StdOut.print(d.get(adjList.indexOf(getNode(adjList, s.peek()))) + " ");
            if (d.get(adjList.indexOf(getNode(adjList, s.peek()))) > time){
                getNode(adjList, s.peek()).getDimension().setCanonNumber(getNode(adjList, s.peek()).getDimension().getCanonNumber()-1);
            }
            StdOut.print(s.pop() + " ");
        }
        StdOut.println();
    }

    public DimensionNode findMin(List<DimensionNode> fringe, List <Integer> d, ArrayList<DimensionNode> adjList) {
        DimensionNode min = fringe.get(0);
        for (int i = 0; i < fringe.size(); i++){
            if (d.get(adjList.indexOf(fringe.get(i))) < d.get(adjList.indexOf(min))){
                min = fringe.get(i);
            }
        }
        
        return fringe.remove(fringe.indexOf(min));
    }

    public DimensionNode getNode(ArrayList<DimensionNode> adjList, int dimension){
        for (int i = 0; i < adjList.size(); i++){
            if (adjList.get(i).getDimension().getDimensionNumber() == dimension)
                return adjList.get(i);
        }
        return null;
    }

    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String anomaliesInputFile = args[3];
        String reportOutputFile = args[4];
        
        Clusters cs = new Clusters();
        cs.readFile(dimensionInputFile);
        DimensionNode[] clusters = cs.getClusters();

        Collider c = new Collider();
        c.makeAdjList(clusters);
        ArrayList<DimensionNode> adjList = c.getAdjList();
        StdIn.setFile(spiderverseInputFile);
        c.insertPeople();
    
        StdIn.setFile(hubInputFile);
        int hub = StdIn.readInt();


        Person[] people = c.getPeople();
        

        ArrayList<Person> anomaliesCollected = new ArrayList<Person>();
        for (Person p : people) {
            if (p.getCurrentDimension() != hub && p.getCurrentDimension() != p.getDimensionalSignature()) {
                anomaliesCollected.add(p);
                p.setCurrentDimension(hub); 
            }
            if (p.getName().equals("Miles") || p.getName().equals("Gwen")){
                anomaliesCollected.add(p);
            }
        }
        StdOut.setFile(reportOutputFile);
        StdIn.setFile(anomaliesInputFile);
        int len = StdIn.readInt();
        GoHomeMachine ghm = new GoHomeMachine();
        for (int i = 0; i < len; i++){
            String name = StdIn.readString();
            int time = StdIn.readInt();
            for (Person person : people){
                if (name.equals(person.getName()))
                    ghm.goHome(person, time, adjList, c.getDimensionNode(hub));
            }
        }
    }
}
