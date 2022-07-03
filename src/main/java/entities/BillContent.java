package entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * ќбъект масс и имен структур в ведомости расхода стали.
 */
public class BillContent {
    /**
     * —писок заголовков просчитанных структур.
     */
    public ArrayList<String> structureTitles = new ArrayList<>();
    /**
     * ћножество блоков определ€емых типом(названием) арматуры (напр. S240, S500, etc.)
     */
    public TreeSet<TypeBarBillBlock> billBlocks = new TreeSet<>(Comparator.comparing(TypeBarBillBlock::getBarType));
    /**
     * Ѕазовые просчитанные структуры, из которых формируем объект контента ведомости расхода стали.
     */
    public ArrayList<CalculatedStructure> calculatedStructures;

    /**
     * ‘ормирование блок с занесением наименований структур и подготовка мапы с диаметрами определнного типа арматуры с нулевыми значени€ми.
     *
     * @param calculatedStructures список просчитанных структур
     */
    public BillContent(ArrayList<CalculatedStructure> calculatedStructures) {
        this.calculatedStructures = calculatedStructures;
        calculatedStructures.forEach((item) -> structureTitles.add(item.getTitle()));
        calculatedStructures.forEach((structure) -> {
            structure.getTypeBarBlocks().forEach((block) -> {
                billBlocks.add(new TypeBarBillBlock(block.getBarType()));
            });
        });
    }

    /**
     * –азмещение значений веса арматуры просчитанных структур в общую карту объекта контента ведомости.
     */
    public void build() {
        this.calculatedStructures.forEach((structure) -> {
            final ArrayList<TypeBarBlock> typeBarBlocks = structure.getTypeBarBlocks();
            typeBarBlocks.forEach((block) -> {
                final String barType = block.getBarType();
                final HashMap<Integer, Double> diameterPositions = block.getDiameterPositionWeights();
                fillCommonBillMapBy(barType, diameterPositions, findIndexOf(structure.getTitle()));
            });
        });
    }

    /**
     * «аполн€ет значени€ весов блока в ведомости по имени типа арматуры указанными в передаваемой карте значений весов
     * @param barType типа арматуры блока
     * @param diameterPositions карта значений с диаметрами-ключами, весами-значени€ми
     * @param index пор€дковый номер структуры
     */
    private void fillCommonBillMapBy(String barType, HashMap<Integer, Double> diameterPositions, int index) {
        final TypeBarBillBlock typeBarBillBlock = this.billBlocks.stream().filter((item) -> item.getBarType().equals(barType)).findFirst().get();
        diameterPositions.forEach((key, value) -> {
            final HashMap<Integer, ArrayList<Double>> billBlockMap = typeBarBillBlock.getDiameterPosition();
            final ArrayList<Double> doubles = billBlockMap.get(key);
            doubles.add(index, value);
        });

    }

    /**
     * »щет пор€дковый номер структуры в списке ведомости по данному наименованию
     * @param name тип арматуры
     * @return индекс int
     */
    private int findIndexOf(String name) {
        return this.structureTitles.indexOf(name);
    }

    /**
     * ¬озвращает строковое-табличное представление заполненной таблицы
     * @return
     */
    private String getContentTableString() {
        final StringBuilder table = new StringBuilder();
        table.append(getTitleBillTable());
        return null;
    }

    /**
     * ¬озвращает шапку ведомости
     * @return строка
     */
    private String getTitleBillTable() {
        return null;
    }

    public BillContent() {

    }

    public ArrayList<String> getStructureTitles() {
        return structureTitles;
    }

    public void setStructureTitles(ArrayList<String> structureTitles) {
        this.structureTitles = structureTitles;
    }

    public TreeSet<TypeBarBillBlock> getBillBlocks() {
        return billBlocks;
    }

    public void setBillBlocks(TreeSet<TypeBarBillBlock> billBlocks) {
        this.billBlocks = billBlocks;
    }
}
