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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    ArrayList<DimensionNode> adjList;
    Person[] people;
    
    public void makeAdjList(DimensionNode[] clusters){
        adjList = new ArrayList<DimensionNode>();
        
        for (int i = 0; i < clusters.length; i++){
            DimensionNode ptr;
            for (ptr = clusters[i]; ptr != null; ptr = ptr.getNext()){
                boolean  found = false;
                for (int j = 0; j < adjList.size(); j++)
                    if (adjList.get(j).getDimension().getDimensionNumber() == ptr.getDimension().getDimensionNumber())
                        found = true;
                if (!found)
                    adjList.add(new DimensionNode(ptr.getDimension(), null)); 
            }
        }

        for (int k = 0; k < clusters.length; k++){
            DimensionNode ptr2;
            for (ptr2 = clusters[k]; ptr2 != null; ptr2 =  ptr2.getNext()){
                DimensionNode ptr3;
                for (int l = 0; l < adjList.size(); l++){
                    if (adjList.get(l).getDimension().getDimensionNumber() == clusters[k].getDimension().getDimensionNumber()){
                        ptr3 =  adjList.get(l);
                        while (ptr3.getNext() != null)
                            ptr3 = ptr3.getNext();
                        ptr3.setNext(new DimensionNode(ptr2.getDimension(), null));
                    }
                }
                for (int m = 0; m < adjList.size(); m++){
                    if (adjList.get(m).getDimension().getDimensionNumber() == ptr2.getDimension().getDimensionNumber()){
                        ptr3 =  adjList.get(m);
                        while (ptr3.getNext() != null)
                            ptr3 = ptr3.getNext();
                        ptr3.setNext(new DimensionNode(clusters[k].getDimension(), null));
                    }
                }
            }
        }
        for (int z = 0; z < adjList.size(); z++){
            removeDuplicates(adjList.get(z));
        }
    }

    public void removeDuplicates(DimensionNode head){
        DimensionNode ptr1 = head, ptr2 = null;

        while (ptr1 != null && ptr1.getNext() != null){
            ptr2 = ptr1;

            while (ptr2.getNext() != null){
                if (ptr1.getDimension().getDimensionNumber() == ptr2.getNext().getDimension().getDimensionNumber()){
                    ptr2.setNext(ptr2.getNext().getNext());
                } else {
                    ptr2 = ptr2.getNext();
                }
            }
            ptr1 = ptr1.getNext();
        }
    }

    public void insertPeople(){
        int len = StdIn.readInt();
        people = new Person[len];
        for (int i = 0; i < len; i++){
            int currentDim = StdIn.readInt();
            String name = StdIn.readString();
            int dimSignature = StdIn.readInt();

            people[i] = new Person(currentDim, name, dimSignature);
        }
    }

    public void printAdjList(){
        for (int i = 0; i < adjList.size(); i++){
            DimensionNode ptr;
            for (ptr = adjList.get(i); ptr != null; ptr = ptr.getNext()){
                StdOut.print(ptr.getDimension().getDimensionNumber() + " ");
            }
            StdOut.println();
        }
    }

    public ArrayList<DimensionNode> getAdjList() {
        return adjList;
    }
    
    public DimensionNode getDimensionNode(int i){
        for (int j = 0; j < adjList.size(); j++){
            for (DimensionNode ptr = adjList.get(j); ptr != null; ptr = ptr.getNext()){
                if (ptr.getDimension().getDimensionNumber() == i){
                    return ptr;
                }
            }
        }
        return null;
    }

    public Person[] getPeople() {
        return people;
    }

    public int getSize(){
        int count = 0;
        for (int i = 0; i < adjList.size(); i++){
            for (DimensionNode ptr = adjList.get(i); ptr != null; ptr = ptr.getNext()){
                count++;
            }
        }
        return count;
    }


    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        String dimensionFile = args[0];
        String spiderVerseFile = args[1];
        String colliderDisplay = args[2];

        
        Clusters cs = new Clusters();
        Collider c = new Collider();
        cs.readFile(dimensionFile);
        
        DimensionNode[] clusters = cs.getClusters();
        
        c.makeAdjList(clusters);

        StdOut.setFile(colliderDisplay);
        c.printAdjList();

        StdIn.setFile(spiderVerseFile);

        c.insertPeople();
    }
}