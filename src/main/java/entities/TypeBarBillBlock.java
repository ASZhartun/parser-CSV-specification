package entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Объект блока ведомости по типу арматуры, состоящий из типа арматуры (напр. S240, S500, etc.) и карты,
 * ключи - возможные диаметры от 3 до 40мм, значение - массив значений Double с весами этих диаметров каждой просчитанной структуры.
 */
public class TypeBarBillBlock {
    /**
     * Определенный тип арматуры для данного блока
     */
    private String barType;

    private HashMap<Integer, ArrayList<Double>> diameterPosition = new HashMap<>();

    public TypeBarBillBlock() {

    }

    /**
     * Инициализация блока с определенным именем и стандартным набором диаметров.
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
