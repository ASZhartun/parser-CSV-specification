package entities;

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

    public HashSet<RebarCage> getExtraUnits() {
        return extraUnits;
    }

    public void setExtraUnits(HashSet<RebarCage> extraUnits) {
        this.extraUnits = extraUnits;
    }
}
