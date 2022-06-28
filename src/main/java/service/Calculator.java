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
     * �������� ������ ��������.
     */
    public void calculate() {
        completeStructures();
        createStructureBills();
    }

    /**
     * �������� �� ������ ��������, ������� ��������� ��������� � �������� ��������� � ������.
     */
    private void createStructureBills() {
        for (Structure structure :
                structures) {
            structureBills.add(createBill(structure));
        }
    }

    /**
     * ������� ��������� �� ���� ���������. ���������, ��������� �������� �� ���� ��������, ��������� �������� �����
     * ��������.
     *
     * @param structure ������ ���������
     * @return ��������� ���������
     */
    private StructureBill createBill(Structure structure) {
        final StructureBill structureBill = new StructureBill();
        structureBill.setTitle(structure.getTitle());
        structureBill.setBars(createBarsBill(structure.getPositions()));
        structureBill.calculateTotal();
        return structureBill;
    }

    /**
     * �������� �� ���� �������� ��������� � ��������� �������� �������� ���� �����������. ���������� ���� � ������
     * ��������.
     *
     * @param positions ������ �������.
     * @return ���� � ������ �������� � ���������� ����������.
     */
    private HashMap<String, Double> createBarsBill(ArrayList<PositionBar> positions) {
        final HashMap<String, Double> map = new HashMap<>();
        positions.forEach((positionBar -> map.merge(positionBar.getRebarType(), positionBar.getWeight(),
                Double::sum)));
        return map;
    }

    /**
     * �������� �� ���� ����������, ���������� ����� ��� ������ ������� ���������.
     */
    private void completeStructures() {
        for (Structure structure :
                structures) {
            competeRebarMeshesWeights(structure.getRebarMeshes());
            completePositionWeights(structure.getPositions());
        }
    }

    /**
     * ������� ��� ���������� ����� ���������, ���������� ����� ������� �����.
     * @param rebarMeshes ������ ���������� ����� ��������� ��� �������
     */
    private void competeRebarMeshesWeights(ArrayList<RebarMesh> rebarMeshes) {
        for (RebarMesh rebarMesh :
                rebarMeshes) {
            calculateWeight(rebarMesh.getBase());
            calculateWeight(rebarMesh.getCross());
        }
    }

    /**
     * ������� ��� ������� ���������, ���������� ����� ��� ������� �������.
     *
     * @param positions ������ ������� ��� �������.
     */
    private void completePositionWeights(ArrayList<PositionBar> positions) {
        for (PositionBar position :
                positions) {
            calculateWeight(position);
        }

    }

    /**
     * ���������� ����� ������ �������.
     *
     * @param position ������ ������� (�������� ��������).
     */
    private void calculateWeight(PositionBar position) {
        double weight = position.getLength() * getMeterWeight(position.getDiameter()) / 1000;
        try {
            position.setWeight(Double.parseDouble(decimalFormat.format(weight).replaceAll(",", ".")));
        } catch (NumberFormatException e) {
            System.out.println("����� � ������������ ����� �������� ����� �� ���� ������, ����");
        }
    }

    /**
     * ���������� ����� ����� ���������. ���������� ������ ������� � ���������� �� ���������. ��� ����������
     * ���������� ����� ����� ���������, ���������� �� ��������� �������� ��������.
     *
     * @param diameter ������ �������
     * @return double ����� ����� ���������
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
