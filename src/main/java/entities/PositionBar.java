package entities;

import service.base.Calculator;

import java.util.stream.Stream;

public class PositionBar implements Cloneable{
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
        final StringBuilder inline = new StringBuilder();
        inline.append(diameter)
                .append(" ")
                .append(rebarType)
                .append(" ")
                .append("ÑÒÁ 1704-2012, L=")
                .append(length);
        return inline.toString();
    }

    public void calcWeight() {
        final RebarWeightGOST rebarWeightGOST = Stream.of(RebarWeightGOST.values()).filter((item) ->
                item.getDiameter() == diameter
        ).findFirst().orElse(RebarWeightGOST.D0);
        weight = length * rebarWeightGOST.getMeterWeight()/1000;
    }

    @Override
    public PositionBar clone() {
        try {
            PositionBar clone = (PositionBar) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private enum RebarWeightGOST {
        D0(0, 0.0),
        D4(4, 0.099),
        D5(5, 0.154),
        D6(6, 0.222),
        D8(8, 0.395),
        D10(10, 0.617),
        D12(12, 0.888),
        D14(14, 1.208),
        D16(16, 1.578),
        D20(20, 2.466),
        D25(25, 3.853),
        D32(32, 6.313);

        private int diameter;
        private double meterWeight;

        RebarWeightGOST(int diameter, double meterWeight) {
            this.diameter = diameter;
            this.meterWeight = meterWeight;
        }

        public int getDiameter() {
            return diameter;
        }

        public void setDiameter(int diameter) {
            this.diameter = diameter;
        }

        public double getMeterWeight() {
            return meterWeight;
        }

        public void setMeterWeight(double meterWeight) {
            this.meterWeight = meterWeight;
        }

    }
}
