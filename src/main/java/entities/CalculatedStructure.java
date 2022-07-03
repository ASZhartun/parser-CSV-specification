package entities;

import java.util.ArrayList;

/**
 * ����������� ��������� ��� ��������� �� � ������ ��������� ���������.
 */
public class CalculatedStructure {
    /**
     * ������������ ���������
     */
    private String title;
    /**
     * ������ � ����������� �������, ����������� �� ���� ��������, �������� � ������ ��������� ���������.
     */
    private ArrayList<TypeBarBlock> typeBarBlocks = new ArrayList<>();
    /**
     * ��������� ����� ���� ���������, �������� � ���������
     */
    private Double totalWeight = 0d;

    public CalculatedStructure() {

    }

    /**
     * ��������� ������� ���� ���������� ���������� ��������
     */
    public void extendBarBlocks() {
        this.typeBarBlocks.forEach((item) -> {
            TypeBarBlock.diameters.forEach((diameter) -> {
                item.getDiameterPositionWeights().putIfAbsent(diameter, 0d);
            });
        });
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TypeBarBlock> getTypeBarBlocks() {
        return typeBarBlocks;
    }

    public void setTypeBarBlocks(ArrayList<TypeBarBlock> typeBarBlocks) {
        this.typeBarBlocks = typeBarBlocks;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void calculateTotalWeight() {
        typeBarBlocks.forEach((item) -> totalWeight += item.getTotalBlockWeight());
    }
}
