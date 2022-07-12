package service.base;

import entities.BillContent;
import entities.CalculatedStructure;
import entities.TypeBarBlock;

import java.util.*;

/**
 * Класс, который формирует csv таблицу ведомости расхода стали.
 */
public class BillPrinter {

    public BillPrinter() {

    }

    /**
     * Формирует ведомость из списка просчитанных структур.
     *
     * @param calculatedStructureCreator держатель просчитанных структур
     * @return строковое представление csv ведомости.
     */
    public String build(CalculatedStructureCreator calculatedStructureCreator) {
        final ArrayList<CalculatedStructure> calculatedStructures = calculatedStructureCreator.getCalculatedStructures();
        calculatedStructures.forEach(CalculatedStructure::extendBarBlocks); // расширили карту диаметров для каждого блока просчитанной структуры
        final BillContent billContent = new BillContent(calculatedStructures);
        billContent.build();
        return billContent.getContentTableString();
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
        return q;
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
