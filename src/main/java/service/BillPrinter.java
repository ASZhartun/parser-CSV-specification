package service;

import entities.BillContent;
import entities.CalculatedStructure;
import entities.TypeBarBlock;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, который формирует csv таблицу ведомости расхода стали.
 */
public class BillPrinter {
    public static void main(String[] args) {
        final TreeMap<Integer, Double> map = new TreeMap<>();
        map.put(20,0d);
        map.put(14,0d);
        map.put(14, 50.37);
        map.merge(14, 49.63, Double::sum);
        final TreeSet<Integer> tree = new TreeSet<>(map.keySet());
        System.out.println(tree);

        System.out.println(map.get(14));

//        Object {
//            final ArrayList<String> title of elements = new ArrayList<String>;
//            final ArrayList<TypeBarExtendingTable> typeBarExtendingTables = new ArrayList<TypeBarExtendingTable>();
//
//            TypeBarExtendingTable {
//                String title of type = "S240";
//                final HashMap<Integer, ArrayList<Double>> diameters with values of all structures = new HashMap<>();
//            }
//        }
    }
    public BillPrinter() {

    }


    public String build(CalculatedStructureCreator calculatedStructureCreator) {
        final ArrayList<CalculatedStructure> calculatedStructures = calculatedStructureCreator.getCalculatedStructures();
        calculatedStructures.forEach(CalculatedStructure::extendBarBlocks); // расширили карту диаметров для каждого блока просчитанной структуры
        final BillContent billContent = new BillContent(calculatedStructures);
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
