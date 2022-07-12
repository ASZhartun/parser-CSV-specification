package entities;

import java.util.ArrayList;
import java.util.HashSet;

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

}
