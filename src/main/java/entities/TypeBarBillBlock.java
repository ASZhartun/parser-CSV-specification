package entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ������ ����� ��������� �� ���� ��������, ��������� �� ���� �������� (����. S240, S500, etc.) � �����,
 * ����� - ��������� �������� �� 3 �� 40��, �������� - ������ �������� Double � ������ ���� ��������� ������ ������������ ���������.
 */
public class TypeBarBillBlock {
    /**
     * ������������ ��� �������� ��� ������� �����
     */
    private String barType;

    private HashMap<Integer, ArrayList<Double>> diameterPosition = new HashMap<>();

    public TypeBarBillBlock() {

    }

    /**
     * ������������� ����� � ������������ ������ � ����������� ������� ���������.
     * @param barType
     */
    public TypeBarBillBlock(String barType) {
        this.barType = barType;
        TypeBarBlock.diameters.forEach((item) -> diameterPosition.putIfAbsent(item, new ArrayList<>()));
    }

    public String getBarType() {
        return barType;
    }

    public void setBarType(String barType) {
        this.barType = barType;
    }

    public HashMap<Integer, ArrayList<Double>> getDiameterPosition() {
        return diameterPosition;
    }

    public void setDiameterPosition(HashMap<Integer, ArrayList<Double>> diameterPositionWeights) {
        this.diameterPosition = diameterPositionWeights;
    }

}
