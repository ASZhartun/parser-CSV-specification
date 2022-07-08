package entities;

import java.util.*;

/**
 * Объект масс и имен структур в ведомости расхода стали.
 */
public class BillContent {
    /**
     * Список заголовков просчитанных структур.
     */
    public ArrayList<String> structureTitles = new ArrayList<>();
    /**
     * Множество блоков определяемых типом(названием) арматуры (напр. S240, S500, etc.)
     */
    public TreeSet<TypeBarBillBlock> billBlocks = new TreeSet<>(Comparator.comparing(TypeBarBillBlock::getBarType));
    /**
     * Базовые просчитанные структуры, из которых формируем объект контента ведомости расхода стали.
     */
    public ArrayList<CalculatedStructure> calculatedStructures;

    /**
     * Формирование блок с занесением наименований структур и подготовка мапы с диаметрами определнного типа арматуры с нулевыми значениями.
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
     * Чистит блоки от нулевых позиций
     */
    private void cleanBlocks() {
        billBlocks.forEach(this::clean);
    }

    /**
     * Чистит мапу от нулевых позиций
     *
     * @param item мапа с диаметрами и весами по всем структурам
     */
    private void clean(TypeBarBillBlock item) {
        item.getDiameterPosition().entrySet().removeIf(this::hasAllZeroValues);
    }

    /**
     * Возвращает true, если все значения списка значений по диаметру равны 0.
     *
     * @param next нода мапы с диаметром и списком значений.
     * @return boolean
     */
    private boolean hasAllZeroValues(Map.Entry<Integer, ArrayList<Double>> next) {
        final ArrayList<Double> values = next.getValue();
        for (Double value : values) {
            if (value != 0) return false;
        }
        return true;
    }

    /**
     * Размещение значений веса арматуры просчитанных структур в общую карту объекта контента ведомости.
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
        cleanBlocks();
        roundBlockWeightValues();
    }

    /**
     * Округляет до двух знаков после запятой веса в карте.
     */
    private void roundBlockWeightValues() {
        billBlocks.forEach(this::round);
    }

    /**
     * Округляет вес текущего диаметра до двух знаков после запятой.
     *
     * @param typeBarBillBlock блок содержащий диаметры и их веса для всех позиций.
     */
    private void round(TypeBarBillBlock typeBarBillBlock) {
        typeBarBillBlock.getDiameterPosition().forEach((key, value) -> value.forEach((weight) ->
                weight = Double.parseDouble(String.format("%.2f", weight).replaceAll(",", "."))
        ));
    }

    /**
     * Заполняет значения весов блока в ведомости по имени типа арматуры указанными в передаваемой карте значений весов
     *
     * @param barType           типа арматуры блока
     * @param diameterPositions карта значений с диаметрами-ключами, весами-значениями
     * @param index             порядковый номер структуры
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
     * Ищет порядковый номер структуры в списке ведомости по данному наименованию
     *
     * @param name тип арматуры
     * @return индекс int
     */
    private int findIndexOf(String name) {
        return this.structureTitles.indexOf(name);
    }

    /**
     * Возвращает строковое представление ведомости.
     *
     * @return строка
     */
    public String getContentTableString() {
        //колонка с именем структуры и колонка "Всего" по структуре
        int columns = 2;
        //количество колонок отведенных под диаметры каждого блока
        columns += billBlocks.stream().mapToInt((item) -> item.getDiameterPosition().size()).sum();
        //колонки под "Итого" по каждому блоку
        columns += billBlocks.size();
        //колонка "Всего"
        columns++;
        //количество колонок диаметров для каждого блока
        final ArrayList<Integer> blockSizes = new ArrayList<>();
        billBlocks.forEach((item) -> blockSizes.add(item.getDiameterPosition().size()));
        return createStaticTitle(columns) +
                createBlocksTitle(blockSizes, columns) +
                createContent(blockSizes, columns);
    }

    /**
     * Создает первые две строки ведомости (неизменяемые).
     *
     * @param columns количество колонок ведомости
     * @return строковое представление первых двух строк таблицы
     */
    private String createStaticTitle(int columns) {
        final StringBuilder staticHead = new StringBuilder();
        for (int i = 0; i < billBlocks.size(); i++) {
            if (i == 0) {
                staticHead.append("Марка элемента;").append("Изделия арматурные");
                staticHead.append(addEmptyCellsWith(columns - 3, " ;"));
                staticHead.append("Всего;\n");
            }
            if (i == 1) {
                staticHead.append(" ;");
                staticHead.append("Арматура класса");
                staticHead.append(addEmptyCellsWith(columns - 2, " ;"));
                staticHead.append('\n');
            }
        }
        return staticHead.toString();
    }

    /**
     * Возвращает строку с заполненной информацией о диаметрах в таблице ведомости.
     *
     * @param blockSizes массив с размерами арматурных блоков
     * @param columns    всего колонок ведомости
     * @return строка-представление данных ведомости расхода стали
     */
    private String createBlocksTitle(ArrayList<Integer> blockSizes, int columns) {
        final StringBuilder blockTitle = new StringBuilder();
        for (int i = 0; i < billBlocks.size(); i++) {
            blockTitle.append(" ;");
            if (i == 0) {
                billBlocks.forEach((item) -> {
                    final String title = item.getBarType();
                    blockTitle
                            .append(title)
                            .append(";")
                            .append(addEmptyCellsWith(item.getDiameterPosition().size(), " ;"));
                });
                blockTitle.append(" ;\n");
            }
            if (i == 1) {
                billBlocks.forEach((item) -> {
                    final String title = item.getBarType();
                    blockTitle
                            .append("СТБ 1704-2012")
                            .append(";")
                            .append(addEmptyCellsWith(item.getDiameterPosition().size(), " ;"));
                });
                blockTitle.append(" ;\n");
            }
        }
        blockTitle.append(addDiameterTitles());
        return blockTitle.toString();
    }

    /**
     * Добавляет сигнатуру диаметров в шапку ведомости.
     * @return строку-представление заголовка диаметров
     */
    private String addDiameterTitles() {
        final StringBuilder diameters = new StringBuilder();
        diameters.append(";");
        billBlocks.forEach((block) -> {
            block.getDiameterPosition().keySet().forEach((key) -> {
                diameters.append(key).append(";");
            });
            diameters.append("Итого;");
        });
        diameters.append(" ;\n");
        return diameters.toString();
    }

    /**
     * Возвращает строки таблицы с диаметрами, значениями весов всех структур.
     *
     * @param blockSizes список размеров арматурных блоков
     * @param columns    число колонок ведомости
     * @return строка-представление данных по всем структурам
     */
    private String createContent(ArrayList<Integer> blockSizes, int columns) {
        final StringBuilder content = new StringBuilder();
        for (int i = 0; i < structureTitles.size(); i++) {
            String title = structureTitles.get(i);
            content.append(title).append(";");
            for (TypeBarBillBlock item :
                    billBlocks) {
                content.append(getStringFromDiametersOf(item, i))
                        .append("=")
                        .append(getTotalWeightFromCurrentBlock(item, i))
                        .append(";");
            }
            content.append("=");
            content.append(String.format("%2f",calculatedStructures.get(i).getTotalWeight()));
            content.append(";\n");
        }
        return content.toString();
    }

    /**
     * Возвращает суммарное значение весов арматуры структуры текущего арматурного блока в виде строки.
     *
     * @param item  арматурный блок
     * @param index порядковый номер структуры ведомости
     * @return строка суммарного веса
     */
    private String getTotalWeightFromCurrentBlock(TypeBarBillBlock item, int index) {
        return String.valueOf(
                item
                        .getDiameterPosition()
                        .values()
                        .stream()
                        .mapToDouble(doubles -> doubles.get(index)).sum()
        ).replaceAll("\\.", ",");
    }

    /**
     * Возвращает строковое представление весов по всем диаметрам для текущей структуры
     * в пределах текущего блока.
     *
     * @param item  арматурный блок ведомости
     * @param index порядковый номер структуры в ведомости
     * @return строка
     */
    private String getStringFromDiametersOf(TypeBarBillBlock item, int index) {
        final StringBuilder structureBlockLine = new StringBuilder();
        final HashMap<Integer, ArrayList<Double>> diameterPosition = item.getDiameterPosition();
        final Set<Map.Entry<Integer, ArrayList<Double>>> diameters = diameterPosition.entrySet();
        for (Map.Entry<Integer, ArrayList<Double>> diameterWithValue :
                diameters) {
            final ArrayList<Double> value = diameterWithValue.getValue();
            structureBlockLine.append("=");
            structureBlockLine.append(String.format("%2f",value.get(index))).append(";");
        }
        return structureBlockLine.toString();
    }

    /**
     * Взвращает строку с заполненными ячейками CSV формата
     *
     * @param i количество ячеек
     * @param s образец заполнения
     * @return строка
     */
    private String addEmptyCellsWith(int i, String s) {
        return String.valueOf(s).repeat(Math.max(0, i));
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
