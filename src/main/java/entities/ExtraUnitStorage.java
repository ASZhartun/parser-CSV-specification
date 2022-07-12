package entities;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * ��������� ��� ���������������� "��������� ������".
 */
public class ExtraUnitStorage {
    /**
     * ������ ��������
     */
    private HashSet<RebarCage> extraUnits = new HashSet<>();


    public ExtraUnitStorage() {

    }

    /**
     * ���������� ���������������� ��������� �������
     *
     * @param item ������ ��������� �������
     */
    public void addUnit(RebarCage item) {
        this.extraUnits.add(item);
    }

}
