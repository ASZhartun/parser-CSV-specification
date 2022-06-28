package service;

import entities.PositionBar;
import entities.RebarMesh;
import entities.Structure;
import entities.StructureBill;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class Calculator {
    public static void main(String[] args) {
        int x = 10;
        double y = 9.99;
        System.out.println(x * y);
        System.out.println(y / x);
    }

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private ArrayList<Structure> structures = new ArrayList<>();
    private ArrayList<StructureBill> structureBills = new ArrayList<>();

    public Calculator() {

    }

    /**
     * ќсновной движок ѕодсчета.
     */
    public void calculate() {
        completeStructures();
        createStructureBills();
    }

    /**
     * ѕроходит по списку структур, создает ведомость структуры и помещает ведомость в список.
     */
    private void createStructureBills() {
        for (Structure structure :
                structures) {
            structureBills.add(createBill(structure));
        }
    }

    /**
     * —оздает ведомость на базе структуры. «аголовок, суммарные величины по типу арматуры, суммарное значение массы
     * арматуры.
     *
     * @param structure данна€ структура
     * @return ведомость структуры
     */
    private StructureBill createBill(Structure structure) {
        final StructureBill structureBill = new StructureBill();
        structureBill.setTitle(structure.getTitle());
        structureBill.setBars(createBarsBill(structure.getPositions()));
        structureBill.calculateTotal();
        return structureBill;
    }

    /**
     * ѕроходит по всем позици€м структуры и суммирует значени€ согласно типу армировани€. ¬озвращает мапу с типами
     * арматуры.
     *
     * @param positions список позиций.
     * @return мапа с типами арматуры и суммарными значени€ми.
     */
    private HashMap<String, Double> createBarsBill(ArrayList<PositionBar> positions) {
        final HashMap<String, Double> map = new HashMap<>();
        positions.forEach((positionBar -> map.merge(positionBar.getRebarType(), positionBar.getWeight(),
                Double::sum)));
        return map;
    }

    /**
     * ѕроходит по всем структурам, обсчитыва€ массу дл€ каждой позиции структуры.
     */
    private void completeStructures() {
        for (Structure structure :
                structures) {
            competeRebarMeshesWeights(structure.getRebarMeshes());
            completePositionWeights(structure.getPositions());
        }
    }

    /**
     * ќбходит все арматурные сетки структуры, обсчитыва€ массу позиций сетки.
     * @param rebarMeshes список арматурных сеток структуры дл€ обсчета
     */
    private void competeRebarMeshesWeights(ArrayList<RebarMesh> rebarMeshes) {
        for (RebarMesh rebarMesh :
                rebarMeshes) {
            calculateWeight(rebarMesh.getBase());
            calculateWeight(rebarMesh.getCross());
        }
    }

    /**
     * ќбходит все позиции структуры, обсчитыва€ массу дл€ текущей позиции.
     *
     * @param positions список позиции дл€ обсчета.
     */
    private void completePositionWeights(ArrayList<PositionBar> positions) {
        for (PositionBar position :
                positions) {
            calculateWeight(position);
        }

    }

    /**
     * ¬ычисление массы данной позиции.
     *
     * @param position данна€ позици€ (стержень арматуры).
     */
    private void calculateWeight(PositionBar position) {
        double weight = position.getLength() * getMeterWeight(position.getDiameter()) / 1000;
        try {
            position.setWeight(Double.parseDouble(decimalFormat.format(weight).replaceAll(",", ".")));
        } catch (NumberFormatException e) {
            System.out.println("ѕроеб с конвертацией после усечение массы до двух знаков, жмых");
        }
    }

    /**
     * ¬озвращает массу метра погонного. —равнивает данный диаметр с диаметрами из множества. ѕри совпадении
     * возвращает массу метра погонного, указанного во множестве согласно диаметру.
     *
     * @param diameter данный диаметр
     * @return double масса метра погонного
     */
    private double getMeterWeight(int diameter) {
        final RebarWeightGOST rebarWeightGOST = Stream.of(RebarWeightGOST.values()).filter((item) ->
                item.getDiameter() == diameter
        ).findFirst().orElse(RebarWeightGOST.D0);
        return rebarWeightGOST.getMeterWeight();
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }

    public ArrayList<StructureBill> getStructureBills() {
        return structureBills;
    }

    public void setStructureBills(ArrayList<StructureBill> structureBills) {
        this.structureBills = structureBills;
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
