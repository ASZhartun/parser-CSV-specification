package entities;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Хранилище для пользовательских "Сборочных единиц".
 */
public class ExtraUnitStorage {
    /**
     * Список объектов
     */
    private HashSet<RebarCage> extraUnits = new HashSet<>();

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

    public HashSet<RebarCage> getExtraUnits() {
        return extraUnits;
    }

    public void setExtraUnits(HashSet<RebarCage> extraUnits) {
        this.extraUnits = extraUnits;
    }
}
