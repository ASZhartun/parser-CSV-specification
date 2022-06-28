package entities;

import java.util.HashMap;

/**
 * ������ ����� � ��������� ������������� ����, ������� ������ � ������ ��������� ���������.
 */
public class TypeBarBlock {
    /**
     * ������������ ��� �������� ��� ������� �����
     */
    private String barType;
    /**
     * ���� � ����������, ��� ������� �������������� ����� ������� ������������� ���� ��������, ���������� � barType.
     */
    private HashMap<Integer, Double> diameterPositionWeights = new HashMap<>();
    /**
     * ��������� ����� �����
     */
    private Double totalBlockWeight = 0d;
    public TypeBarBlock() {

    }

    public String getBarType() {
        return barType;
    }

    public void setBarType(String barType) {
        this.barType = barType;
    }

    public HashMap<Integer, Double> getDiameterPositionWeights() {
        return diameterPositionWeights;
    }

    public void setDiameterPositionWeights(HashMap<Integer, Double> diameterPositionWeights) {
        this.diameterPositionWeights = diameterPositionWeights;
    }

    public Double getTotalBlockWeight() {
        return totalBlockWeight;
    }

    public void setTotalBlockWeight(Double totalBlockWeight) {
        this.totalBlockWeight = totalBlockWeight;
    }
}
