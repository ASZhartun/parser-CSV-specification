package entities;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

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
