package entities;

import java.util.ArrayList;

/**
 * Хранилище для пользовательских "Сборочных единиц".
 */
public class ExtraUnitStorage {
    /**
     * Список объектов
     */
    private ArrayList<RebarCage> extraUnits = new ArrayList<>();

    public ExtraUnitStorage() {

    }

    /**
     * Добавление пользовательской сборочной единицы
     *
     * @param item объект сборочной единицы
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
