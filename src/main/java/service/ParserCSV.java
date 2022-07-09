package service;

import entities.*;
import exceptions.CreatingTableException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ParserCSV {
    public static void main(String[] args) {
        String sample = "\"12-5650\"\t \t12 S500 СТБ 1704-2012, L=5650\t\"116\"\t\"5,02\"\t ";
        sample = sample.replaceAll("\"", "");
        System.out.println(sample);
    }

    /**
     * Вспомогательный парсер арматурных сеток по ГОСТ 23279-2012.
     */

    private RebarMeshParser rebarMeshParser;
    /**
     * Список, содержащий все структуры, которые описаны в общей таблице.
     */
    private ArrayList<Structure> structures = new ArrayList<>();
    /**
     * <p>Примеры поля, которое соответствует regex'у:</p>
     * <ul>
     *     <li><pre>16 S500 СТБ 1704-2012, L=5999</pre></li>
     *     <li><pre>8  S500   СТБ 1704-2012,L=5999</pre></li>
     *     <li><pre>8    S240   СТБ 1704-2012, L=600</pre></li>
     *     <li><pre>8    S240   СТБ1704-2012, L=350</pre></li>
     * </ul>
     */
    private static final String REGEX_POSITION_NAME_WITH_LENGTH = "[0-9]{1,2}[ ]+[S,A,А][0-9]{3}[ ]+СТБ[ ]?1704-2012,[ ]?[L][=][0-9]+";


    public ParserCSV() {

    }

    /**
     * <h2>Основной движок парсера.</h2>
     * <h3>Принцип работы</h3>
     * <ol>
     *     <li>Формируем объект общей таблицы из строки, которую получаем при чтении CSV-файла.</li>
     *     <li>Общую таблицу разбиваем на частные таблицы структур.</li>
     *     <li>Каждую таблицу структуры парсим и создаем объект структуры, который кладем в список структур для
     *      дальнейшей работы.</li>
     * </ol>
     *
     * @param content данные, читаемые из csv файла.
     */
    public void parseTable(ArrayList<String> content) {
        BlockTable blockTable = formTable(content);
        ArrayList<BlockTable> structureBlocks = getBlockStructureListFromCommonTable(blockTable);
        loopParseStructureBlocksData(structureBlocks);
    }

    /**
     * Перегоняет строку в объект таблицы. Таблица имеет объекты строк
     *
     * @param content коллекция строк, полученный из csv файла
     * @return объект таблицы
     */
    private BlockTable formTable(ArrayList<String> content) {
        content.remove(0);
        content.remove(0);
        final BlockTable blockTable = new BlockTable();
        for (String stringLine :
                content) {
            final String[] split = stringLine.split(";");
            final Line line;
            try {
                line = new Line(split[0], split[1], split[2], split[3], split[4], split[5]);
                blockTable.getLines().add(line);
            } catch (CreatingTableException e) {
                System.out.println("Формирование таблицы в говне почему-то");
            } catch (Exception e) {
                System.out.println("Посос со своим эксепшеном и с формированием таблицы, лох");
            }
        }
        return blockTable;
    }

    /**
     * Формирует список таблиц, каждая из которых имеет по своей единственной структуре.
     *
     * @param blockTable общая таблица
     * @return список частных таблиц
     */
    private ArrayList<BlockTable> getBlockStructureListFromCommonTable(BlockTable blockTable) {
        final ArrayList<BlockTable> blockTables = new ArrayList<>();
        int upBorderIndex = 0;
        int downBorderIndex;
        final ArrayList<Line> commonLines = blockTable.getLines();
        for (Line line : commonLines) {
            if (commonLines.indexOf(line) != 0) {
                if (lineHasStructureName(line) || commonLines.indexOf(line) == commonLines.size() - 1) {
                    downBorderIndex = blockTable.getLines().indexOf(line);
                    final BlockTable temp = new BlockTable();
                    temp.setLines(
                            new ArrayList<>(
                                    commonLines
                                            .subList(upBorderIndex, downBorderIndex + 1)
                            )
                    );
                    blockTables.add(temp);
                    upBorderIndex = commonLines.indexOf(line);
                }
            }
        }
        return blockTables;

    }

    /**
     * проверяет есть ли в строке заголовок элемента(структуры).
     *
     * @param line строка таблицы
     * @return да, если имеет.
     */
    private boolean lineHasStructureName(Line line) {
        if (titleIsWrappedQuotes(line)) {
            line.setName(deleteQuotes(line.getName()));
        }
        if (isEmptyAroundPotentialStructureName(line)) {
            if (isNotStandardTitles(line)) {
                return startedWithBigLetter(line);
            }
        }
        return false;
    }

    /**
     * Удаляет внешние ковычки
     *
     * @param name
     * @return строка без ковычек
     */
    private String deleteQuotes(String name) {
        return name.substring(1, name.length() - 1);
    }

    /**
     * Проверяет начинается ли строка с ковычек (обернута ли строка в ковычки)
     *
     * @param line строка таблицы
     * @return true/false
     */
    private boolean titleIsWrappedQuotes(Line line) {
        return line.getName().indexOf("\"") == 0;
    }

    /**
     * Проверяет начинается ли поле "Наименование" в строке с большой русской буквы.
     *
     * @param line проверяемая строка
     * @return да, если да
     */
    private boolean startedWithBigLetter(Line line) {
        int numericValueOfFirstLetter = line.getName().charAt(0);
        return numericValueOfFirstLetter > 1039 && numericValueOfFirstLetter < 1072;
    }

    /**
     * Проверяет является ли поле строкой отличной от названий разделов таблицы.
     *
     * @param line проверяемая строка
     * @return да, если является отличной
     */
    private boolean isNotStandardTitles(Line line) {
        return !line.getName().equals("Материалы") &&
                !line.getName().equals("Сборочные единицы") &&
                !line.getName().equals("Детали");
    }

    /**
     * Проверяет строку на заполнение только поля "Наименование"
     *
     * @param line проверяемая строка
     * @return да, если заполнено <b>только</b> поле "Наименование"
     */
    private boolean isEmptyAroundPotentialStructureName(Line line) {
        return line.getPos().equals(" ") && line.getDescription().equals(" ")
                && line.getQuantity().equals(" ") && line.getNote().equals(" ");
    }

    /**
     * Добавляет распаршенную структуру в список структур, содержащихся в общей таблице.
     *
     * @param structureBlocksData список частных таблиц, содержащих описание структур.
     */
    private void loopParseStructureBlocksData(ArrayList<BlockTable> structureBlocksData) {
        for (BlockTable block :
                structureBlocksData) {
            structures.add(parseCurrentStructureBlock(block));
        }
    }

    /**
     * Формирует из таблицы со структурой объект структуры(элемента).
     *
     * @param block таблица с данными структуры
     * @return структура
     */
    private Structure parseCurrentStructureBlock(BlockTable block) {
        final Structure structure = new Structure();
        structure.setTitle(parseBlockTitle(block));
        structure.setConcreteVolume(parseBlockConcreteVolume(block));
        structure.setConcreteDefinition(parseBlockConcreteDefinition(block));
        parseBlockBarsPositions(block, structure);
        return structure;
    }

    /**
     * Возвращает список строк из блока структуры, содержащий описание марки бетона
     *
     * @param block блок, описывающий структуру
     * @return массив строк, описывающий бетон
     */
    private String parseBlockConcreteDefinition(BlockTable block) {
        String definition = "";
        int indexConcrete = getIndexOfMaterialsPart(block) + 1;
        final ArrayList<Line> lines = block.getLines();
        if (!lines.get(indexConcrete).getName().equals(" ")) {
            definition = (lines.get(indexConcrete).getName());

        }
        return definition;
    }

    /**
     * Пробегает по позициям и вычленяет позиции, которые подходят как стержни или как сетки по ГОСТ.
     * Парсит в зависимости от того, что это. Формирует как позицию стержня. Добавляет ее в список.
     * Список возвращается.
     *
     * @param block таблица структуры.
     */
    private void parseBlockBarsPositions(BlockTable block, Structure structure) {
        removeWrappingQuotes(block);
        final ArrayList<PositionBar> positionBars = new ArrayList<>();
        final ArrayList<RebarMesh> rebarMeshes = new ArrayList<>();
        final int downEdgeOfPositions = getIndexOfMaterialsPart(block);
        final ArrayList<Line> lines = block.getLines();
        for (int i = 1; i < downEdgeOfPositions; i++) {
            final Line line = lines.get(i);
            if (line.getDescription().equals("ГОСТ 23279-2012")) {
                final RebarMesh rebarMesh = rebarMeshParser.build(line.getName(), line.getQuantity());
                rebarMeshes.add(rebarMesh);
            } else if (isPosition(line.getName())) {
                final PositionBar temp = parsePositionBar(line);
                positionBars.add(temp);
            }
        }
        structure.setPositions(positionBars);
        structure.setRebarMeshes(rebarMeshes);
    }

    /**
     * Удаляет ковычки, которые оборачивают значения в ячейках при чтении из csv файла полученного из ранней версии автокад.
     *
     * @param block блок со структурой
     */
    private void removeWrappingQuotes(BlockTable block) {
        block.getLines().stream().forEach((item) -> {
            item.setPos(item.getPos().replaceAll("\"", ""));
            item.setDescription(item.getDescription().replaceAll("\"", ""));
            item.setName(item.getName().replaceAll("\"", ""));
            item.setQuantity(item.getQuantity().replaceAll("\"", ""));
            item.setWeight(item.getWeight().replaceAll("\"", ""));
            item.setNote(item.getNote().replaceAll("\"", ""));
        });
    }


    /**
     * Парсит строку с описанием позиции, создает и возвращает объект полученной позиции.
     *
     * @param line объект строки, в которой находится позиция с арматурным стержней.
     * @return объект позиции стержня
     */
    private PositionBar parsePositionBar(Line line) {
        final PositionBar positionBar = new PositionBar();
        positionBar.setQuantity(getQuantityAndParseToInt(line));
        setDiameterAndTypeAndLengthTo(positionBar, line);
        return positionBar;
    }

    /**
     * Устанавливает диаметр, тип арматуры, длину в позицию.
     *
     * @param positionBar позиция, в которую устанавливаем значения
     * @param line        строка, содержащая данные по позиции
     */
    private void setDiameterAndTypeAndLengthTo(PositionBar positionBar, Line line) {
        final String[] split = line.getName().split("[ ]+");
        positionBar.setDiameter(extractDiameter(split[0]));
        positionBar.setRebarType(split[1]);
        positionBar.setLength(extractLengthFrom(split[4]));
    }

    /**
     * Извлекает диаметр из строки и конвертирует в int.
     *
     * @param s строка с диаметром
     * @return диаметр типа int
     */
    private int extractDiameter(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Диаметр факапнулся при извлечении из \"Наименование\"");
        }
        return 0;
    }

    /**
     * Извлекает длину из строки "L=9999": int x = 9999;
     *
     * @param s строка с длиной
     * @return длина типа int
     */
    private int extractLengthFrom(String s) {
        try {
            return Integer.parseInt(s.split("=")[1]);
        } catch (NumberFormatException e) {
            System.out.println("Длина факапнулась при извлечении из \"Наименование\"");
        }
        return 0;
    }

    /**
     * Ищет в строке ячейку "Количество". Забирает значение строки и конвертирует в int. Возвращает полученное значение.
     *
     * @param line объект строки, описывающая позицию.
     * @return int количество позиции.
     */
    private int getQuantityAndParseToInt(Line line) {
        try {
            return Integer.parseInt(line.getQuantity());
        } catch (NumberFormatException e) {
            System.out.println("Количество стержней не перевелось в Интегер");
        }
        return 0;
    }

    /**
     * Проверяет, что наименование соответствует позиции стержня.
     *
     * @param name контент поля "Наименование"
     * @return true, если соответствует
     */
    private boolean isPosition(String name) {
        final Pattern compile = Pattern.compile(REGEX_POSITION_NAME_WITH_LENGTH);
        final Matcher matcher = compile.matcher(name);
        return matcher.matches();
    }

    /**
     * Находит начало секции "Материалы". Находит значение объема бетона. Конвертирует в double и отдает.
     *
     * @param block таблица структуры
     * @return объема бетона типа double
     */
    private Double parseBlockConcreteVolume(BlockTable block) {
        int indexConcrete = getIndexOfMaterialsPart(block) + 1;
        String concreteValue = block.getLines().get(indexConcrete).getNote();
        return extractDoubleValueOfConcreteVolumeFrom(concreteValue);
    }

    /**
     * Достает из строки объем бетона типа double.
     *
     * @param concreteValue строка с объемом бетона. Напр., "2,3м3".
     * @return объем бетона типа double
     */
    private Double extractDoubleValueOfConcreteVolumeFrom(String concreteValue) {
        String pattern = "[0-9],[0-9]{1,2}";
        final Pattern compile = Pattern.compile(pattern);
        final Matcher matcher = compile.matcher(concreteValue);
        if (matcher.find()) {
            final String group = matcher.group(0);
            final String formatGroup = group.replaceAll(",", ".");
            return Double.parseDouble(formatGroup);
        }
        return (double) 0;
    }

    /**
     * Возвращает индекс начала секции "Материалы"
     *
     * @param block таблица структуры
     * @return индекс типа int
     */
    private int getIndexOfMaterialsPart(BlockTable block) {
        final ArrayList<Line> lines = block.getLines();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).getName().equals("Материалы")) return i;
        }
        return 0;
    }

    /**
     * Получаем последнее слово из наименования структуры (аббревиатура)
     *
     * @param block таблица структуры
     * @return последнее слово из наименования
     */
    private String parseBlockTitle(BlockTable block) {
        if (block.getLines().get(0).getName().indexOf("\"") == 0) {
            block.getLines().get(0).setName(block.getLines().get(0).getName().substring(1,
                    block.getLines().get(0).getName().length() - 1));
        }
        final String name = block.getLines().get(0).getName();
        final String[] s = name.split(" ");
        return s[s.length - 1];
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }

    public RebarMeshParser getRebarMeshParser() {
        return rebarMeshParser;
    }

    @Autowired
    public void setRebarMeshParser(RebarMeshParser rebarMeshParser) {
        this.rebarMeshParser = rebarMeshParser;
    }
}
