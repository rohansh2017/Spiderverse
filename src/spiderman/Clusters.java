package spiderman;
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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    private DimensionNode[] clusters;
    

    public void readFile(String fileName){
        
        StdIn.setFile(fileName);

        int numDimensions = StdIn.readInt();
        int tableSize = StdIn.readInt();
        int maxCapacity = StdIn.readInt();
        

        clusters = new DimensionNode[tableSize];

        for (int i = 0; i < numDimensions; i++){
            int dimNum = StdIn.readInt();
            int canonNum = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            double average = 0.0;

            DimensionNode d = new DimensionNode(new Dimension(dimNum, canonNum, dimWeight), null);

            if (clusters[dimNum % tableSize] == null)
                clusters[dimNum % tableSize] = d;
            else{
                d.setNext(clusters[dimNum % tableSize]);
                clusters[dimNum % tableSize] = d;
            }

            for (DimensionNode dNode : clusters)
                average += findSize(dNode)*1.0/tableSize;
            

            if (average >= maxCapacity){
                DimensionNode[] temp = new DimensionNode[2*tableSize];
                tableSize *= 2;

                for (int j = 0; j < clusters.length; j++){
                    DimensionNode ptr = clusters[j];
                    while (ptr != null){
                        dimNum = ptr.getDimension().getDimensionNumber();
                        DimensionNode dNode = new DimensionNode(ptr.getDimension(), null);

                        if (temp[dimNum % tableSize] == null)
                            temp[dimNum % tableSize] = new DimensionNode(ptr.getDimension(), null);
                        else {
                            
                            dNode.setNext(temp[dimNum % tableSize]);
                            temp[dimNum % tableSize] = dNode;
                        }
                        ptr = ptr.getNext();
                    }
                }
                clusters = temp;
            }
        }

        //uniting dimensions
        for (int x = 0; x < clusters.length; x++){
            DimensionNode lastPtr = clusters[x];
            
            while (lastPtr.getNext() != null)
                lastPtr = lastPtr.getNext();
            
            if (x == 0){
                lastPtr.setNext(new DimensionNode(clusters[clusters.length-1].getDimension(), null));
                lastPtr.getNext().setNext(new DimensionNode(clusters[clusters.length-2].getDimension(), null));
            } else if (x == 1) {
                lastPtr.setNext(new DimensionNode(clusters[0].getDimension(), null));
                lastPtr.getNext().setNext(new DimensionNode(clusters[clusters.length-1].getDimension(), null));
            } else {
                lastPtr.setNext(new DimensionNode(clusters[x-1].getDimension(), null));
                lastPtr.getNext().setNext(new DimensionNode(clusters[x-2].getDimension(), null));
            }
        }
    }
   
    public DimensionNode[] getClusters() {
        return clusters;
    }

    public int getSize(){
        int count = 0;

        for (int i = 0; i < clusters.length; i++){
            DimensionNode ptr = clusters[i];
            while (ptr != null){
                count++; 
                ptr = ptr.getNext(); 
            }
        }
        return count;
    }
    

    public int findSize(DimensionNode d){
        DimensionNode temp = d;
        int count = 0;
        while (temp != null) {
            count++;
            temp = temp.getNext();
        }
        return count;
    }

    public void printTable(){
        if (clusters != null){
            for (int i = 0; i < clusters.length; i++){
                DimensionNode ptr = clusters[i];
                while (ptr != null){
                    StdOut.print(ptr.getDimension().getDimensionNumber() + " ");
                    ptr = ptr.getNext();
                }
                StdOut.println();
            }
        }
    }
    public static void main(String[] args) {
        
        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }
        String inputFile = args[0];
        String outputFile = args[1];
        Clusters cs = new Clusters();
        cs.readFile(inputFile);

        StdOut.setFile(outputFile);
        cs.printTable();

        
    }
}
