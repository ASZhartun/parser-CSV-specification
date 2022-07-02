package service;

import entities.CalculatedStructure;
import entities.TypeBarBlock;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * �����, ������� ��������� csv ������� ��������� ������� �����.
 */
public class BillPrinter {
    public BillPrinter() {

    }


    public String build(CalculatedStructureCreator calculatedStructureCreator) {
        int billColumnQuantity = getBillColumnQuantity(calculatedStructureCreator.getCalculatedStructures()) + 1; // +1 ��� ��������� ��������� (������� "������������(�����)")
        return null;
    }

    /**
     * ���������� ������������ ���������� ������� �� ���� ��������� ������������ ��������
     *
     * @param calculatedStructures ������ ������������ ��������
     * @return integer �������
     */
    private int getBillColumnQuantity(ArrayList<CalculatedStructure> calculatedStructures) {
        int columns = 0;
        for (CalculatedStructure item :
                calculatedStructures) {
            int temp = getQuantityColumnsOfCurrent(item);
            columns = Math.max(columns, temp);
        }
        return columns;
    }

    /**
     * ���������� ���������� �������, ��������� ��� ������ ������������ ���������
     *
     * @param item ������ ������������ ���������
     * @return ���������� �������
     */
    private int getQuantityColumnsOfCurrent(CalculatedStructure item) {
        int q = 1;
        final ArrayList<TypeBarBlock> typeBarBlocks = item.getTypeBarBlocks();
        q += typeBarBlocks.size();
        q += getColumnsQuantityOfEach(typeBarBlocks);
        return q;
    }

    /**
     * ���������� ���������� �������, ��������� ��� ������ ������ ������������ ��������� �� ���� ��������
     *
     * @param typeBarBlocks ������ �������� ����� ������������ ���������
     * @return ����� ���������� �������
     */
    private int getColumnsQuantityOfEach(ArrayList<TypeBarBlock> typeBarBlocks) {
        int q = 0;
        for (TypeBarBlock item :
                typeBarBlocks) {
            q += getQuantityOfDiameters(item.getDiameterPositionWeights());
        }
        return 0;
    }

    /**
     * ���������� ���������� ���������� ���������, ������������ � ����� ������������� ���� ��������
     *
     * @param diameterPositionWeights ����� ��������� � �� ������
     * @return ���������� �������
     */
    private int getQuantityOfDiameters(HashMap<Integer, Double> diameterPositionWeights) {
        return diameterPositionWeights.keySet().size();
    }
}
