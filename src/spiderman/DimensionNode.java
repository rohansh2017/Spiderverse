package spiderman;

public class DimensionNode {

    private Dimension dimension;
    private DimensionNode next;

    public DimensionNode(Dimension dimension, DimensionNode next) {
        this.dimension = dimension;
        this.next = next;
    }

    public Dimension getDimension() { return dimension; }
    public void setDimension(Dimension dimension) { this.dimension = dimension; }

    public DimensionNode getNext() { return next; }
    public void setNext(DimensionNode next) { this.next = next; }

    public int getSize(){
        DimensionNode ptr = new DimensionNode(this.dimension, this.next);
        int count = 0;
        while (ptr != null){
            ptr = ptr.getNext();
            count++;
        }
        return count;
    }

    public DimensionNode getElement(int i){
        DimensionNode ptr = new DimensionNode(this.dimension, this.next);
        for (int j = 0; j < i-1; j++){
            ptr = ptr.getNext();
        }

        return ptr;
    }

    public boolean equals(Object other){
        if (other instanceof  DimensionNode){
            DimensionNode dnOther = (DimensionNode) other;
            if (this.dimension.getDimensionNumber() == dnOther.getDimension().getDimensionNumber()) return true;
        }
        return false;
    }
}