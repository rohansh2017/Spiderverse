package spiderman;

public class Dimension {
    
    private int dimensionNumber;
    private int canonNumber;
    private int weight;

    public Dimension(int dimensionNumber, int canonNumber, int weight){
        this.dimensionNumber = dimensionNumber;
        this.canonNumber = canonNumber;
        this.weight = weight; 
    }

    public int getDimensionNumber() { return dimensionNumber; }
    public void setDimensionNumber(int dimensionNumber) { this.dimensionNumber = dimensionNumber; }

    public int getCanonNumber() { return canonNumber; }
    public void setCanonNumber(int canonNumber) { this.canonNumber = canonNumber; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
}
