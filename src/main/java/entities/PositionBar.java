package entities;

public class PositionBar {
    String rebarType;
    int diameter;
    int length;
    int quantity;
    double weight;

    public PositionBar(String rebarType, int diameter, int length, int quantity) {
        this.rebarType = rebarType;
        this.diameter = diameter;
        this.length = length;
        this.quantity = quantity;
    }

    public PositionBar(String rebarType, int diameter, int length) {
        this.rebarType = rebarType;
        this.diameter = diameter;
        this.length = length;
    }

    public PositionBar() {

    }

    public String getRebarType() {
        return rebarType;
    }

    public void setRebarType(String rebarType) {
        this.rebarType = rebarType;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PositionBar{" +
                "rebarType='" + rebarType + '\'' +
                ", diameter=" + diameter +
                ", length=" + length +
                ", quantity=" + quantity +
                ", weight=" + weight +
                '}';
    }
}
