package spiderman;

public class Person {
    int currentDimension;
    String name;
    int dimensionalSignature;

    public Person(int currentDimension, String name, int dimensionalSignature) {
        this.currentDimension = currentDimension;
        this.name = name;
        this.dimensionalSignature = dimensionalSignature;
    }
    
    public void setCurrentDimension(int currentDimension) {
        this.currentDimension = currentDimension;
    }

    public int getCurrentDimension() {
        return currentDimension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDimensionalSignature(int dimensionalSignature) {
        this.dimensionalSignature = dimensionalSignature;
    }

    public int getDimensionalSignature() {
        return dimensionalSignature;
    }
}
