package entities;

import java.util.ArrayList;

/**
 * ��������� ��� ���������������� "��������� ������".
 */
public class ExtraUnitStorage {
    /**
     * ������ ��������
     */
    private ArrayList<RebarCage> extraUnits = new ArrayList<>();

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

    public ArrayList<RebarCage> getExtraUnits() {
        return extraUnits;
    }

    public void setExtraUnits(ArrayList<RebarCage> extraUnits) {
        this.extraUnits = extraUnits;
    }
}
