package service;

import entities.PositionBar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;


public class Calculator {
    private ArrayList<PositionBar> positions;
    private double totalWeight;
    private HashMap<Integer, Double> S500 = new HashMap<>();
    private HashMap<Integer, Double> S240 = new HashMap<>();

    public Calculator(ArrayList<PositionBar> positions) {
        this.positions = positions;
        calculatePositionWeights();
        calculateTotalWeight();
        calculateSteelBill();
    }

    public Calculator() {

    }

    private void calculateSteelBill() {
        positions.forEach((item) -> {
            if (item.getRebarType().equals("S500")) {
                if (!S500.containsKey(item.getDiameter())) {
                    S500.put(item.getDiameter(), item.getWeight() * item.getQuantity());
                } else {
                    S500.put(item.getDiameter(), S500.get(item.getDiameter()) + item.getWeight() * item.getQuantity());
                }
            } else {
                if (!S240.containsKey(item.getDiameter())) {
                    S240.put(item.getDiameter(), item.getWeight() * item.getQuantity());
                } else {
                    S240.put(item.getDiameter(), S240.get(item.getDiameter()) + item.getWeight() * item.getQuantity());
                }
            }
        });
        System.out.println();
    }

    private void calculateTotalWeight() {
        totalWeight = this.positions.stream().mapToDouble(item -> item.getWeight() * item.getQuantity()).sum();
    }

    private void calculatePositionWeights() {
        this.positions.stream().forEach((item) -> {
            final double weightMeter = runningMeter(item.getDiameter());
            double result = weightMeter * item.getLength();
            result = result / 1000;
            result = Double.parseDouble(BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP).toString());
            item.setWeight(result);
        });
    }

    private double runningMeter(int diameter) {
        final EnumSet<RebarWeightGOST> rebarWeightGOSTS = EnumSet.allOf(RebarWeightGOST.class);
        return rebarWeightGOSTS
                .stream()
                .filter((item) -> item.getDiameter() == diameter)
                .findFirst()
                .orElse(RebarWeightGOST.D0)
                .getMeterWeight();
    }

    public ArrayList<PositionBar> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<PositionBar> positions) {
        this.positions = positions;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public HashMap<Integer, Double> getS500() {
        return S500;
    }

    public void setS500(HashMap<Integer, Double> s500) {
        S500 = s500;
    }

    public HashMap<Integer, Double> getS240() {
        return S240;
    }

    public void setS240(HashMap<Integer, Double> s240) {
        S240 = s240;
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
