package service;

import entities.CalculatedStructure;
import entities.TypeBarBlock;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс, который формирует csv таблицу ведомости расхода стали.
 */
public class BillPrinter {
    public BillPrinter() {

    }


    public String build(CalculatedStructureCreator calculatedStructureCreator) {
        int billColumnQuantity = getBillColumnQuantity(calculatedStructureCreator.getCalculatedStructures()) + 1; // +1 для заголовка структуры (колонка "Наименование(Марка)")
        return null;
    }

    /**
     * Возвращает максимальное количество колонок из всех доступных просчитанных структур
     *
     * @param calculatedStructures список просчитанных структур
     * @return integer колонок
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
     * Возвращает количество колонок, требуемых под данные просчитанной структуры
     *
     * @param item объект просчитанной структуры
     * @return количество колонок
     */
    private int getQuantityColumnsOfCurrent(CalculatedStructure item) {
        int q = 1;
        final ArrayList<TypeBarBlock> typeBarBlocks = item.getTypeBarBlocks();
        q += typeBarBlocks.size();
        q += getColumnsQuantityOfEach(typeBarBlocks);
        return q;
    }

    /**
     * Возвращает количество колонок, требуемые под данные блоков просчитанной структуры по типу арматуры
     *
     * @param typeBarBlocks список объектов блока просчитанной структуры
     * @return общее количество колонок
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
     * Возвращает количество уникальных диаметров, содержащихся в блоке определенного типа арматуры
     *
     * @param diameterPositionWeights карта диаметров с их весами
     * @return количество позиций
     */
    private int getQuantityOfDiameters(HashMap<Integer, Double> diameterPositionWeights) {
        return diameterPositionWeights.keySet().size();
    }
}
