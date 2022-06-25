package service;

import entities.BlockTable;
import entities.Line;
import entities.PositionBar;
import entities.Structure;
import exceptions.CreatingTableException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParserCSV {
    public static void main(String[] args) {
        int c = '0';
        int x = '9';
        System.out.println(c + " " + x);
        int z = 'A';
        int v = 'Z';
        System.out.println(z + " " + v);
        if (x > 56) {
            System.out.println("success");
        }
    }

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
    /**
     * <p>Примеры поля, которое соответствует regex'у:</p>
     * <ul>
     *      <li><pre>4С %%C5S500-100/%%C5S500-100 65x145</pre></li>
     *      <li><pre>4С%%C5S500-100/%%C5S500-100  65x145</pre></li>
     *      <li><pre>1С %%12S500-200/%%8S500-600 105x145</pre></li>
     *      <li><pre>1С %%12S240-200/%%8S240-600 105x145</pre></li>
     *      <li><pre>2С%%12S500-200/%%12S500-200 125x130</pre></li>
     * </ul>
     */
    private static final String REGEX_NAME_OF_REBAR_MESH_BY_GOST = "[0-9][C,С][р]?[ ]?%%[C,С]?[0-9]+S[2,5][4,0]0-([0-9]{3}\\([0-9]{3}\\)|[0-9]{2,3})\\/%%[C,С]?[0-9]+S[2,5][4,0]0-([0-9]{3}\\([0-9]{3}\\)|[0-9]{2,3})[ ]+?[0-9]{2,3}[x,х][0-9]{2,3}";
    /**
     * <ul>
     *     <li>[C5S500-100,C5S500-100]</li>
     *     <li>[12S500-200,8S500-600]</li>
     *     <li>[12S500-200,12S500-200]</li>
     * </ul>
     */
    private static final String REGEX_DIAMETER_AND_STEP_BARS_OF_REBAR_MESH = "[C,С]?[0-9]+S[2,5][4,0]0-([0-9]{3}\\([0-9]{3}\\)|[0-9]{2,3})";
    /**
     * <ul>
     *     <li>65x145</li>
     *     <li>105x145</li>
     *     <li>125x130</li>
     * </ul>
     */
    private static final String REGEX_LENGTH_OF_BARS_OF_REBAR_MESH = "[0-9]{2,3}[X,Х,x,х][0-9]{2,3}";
    /**
     * <ul>
     *     <li>[65,145]</li>
     *     <li>[105,145]</li>
     *     <li>[125,130]</li>
     * </ul>
     */
    private static final String REGEX_FOR_UNIT_LENGTH_OF_BARS_OF_REBAR_MESH = "[0-9]{2,3}";

    /**
     * Из "C5S500-100" получает 3 группы: "C5", "S500","100"
     */
    private static final String REGEX_FOR_INFO_ABOUT_WIRE_OF_REBAR_MESH = "([A-Za-zА-Яа-я]{1,2}[0-9])(S[0-9]{3})-([0-9]{2,3})";
    /**
     * Из 12S500-200 получает 3 группы: "12", "S500", "200"
     */
    private static final String REGEX_FOR_INFO_ABOUT_BAR_OF_REBAR_MESH = "([0-9]{1,2})(S[0-9]{3})-([0-9]{1,3})";

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
                if (lineHasStructureName(line)) {
                    downBorderIndex = blockTable.getLines().indexOf(line);
                    final BlockTable temp = new BlockTable();
                    temp.setLines(
                            new ArrayList<>(
                                    commonLines
                                            .subList(upBorderIndex, downBorderIndex)
                            )
                    );
                    blockTables.add(temp);
                    upBorderIndex = commonLines.indexOf(line);
                }
            }
        }
        System.out.println(blockTables.get(0).getLines());
        return blockTables;

    }

    /**
     * проверяет есть ли в строке заголовок элемента(структуры).
     *
     * @param line строка таблицы
     * @return да, если имеет.
     */
    private boolean lineHasStructureName(Line line) {
        if (isEmptyAroundPotentialStructureName(line)) {
            if (isNotStandardTitles(line)) {
                return startedWithBigLetter(line);
            }
        }
        return false;
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
        structure.setPositions(parseBlockBarsPositions(block));
        return structure;
    }

    /**
     * Пробегает по позициям и вычленяет позиции, которые подходят как стержни или как сетки по ГОСТ.
     * Парсит в зависимости от того, что это. Формирует как позицию стержня. Добавляет ее в список.
     * Список возвращается.
     *
     * @param block таблица структуры.
     * @return список позиций стержней.
     */
    private ArrayList<PositionBar> parseBlockBarsPositions(BlockTable block) {
        final ArrayList<PositionBar> positionBars = new ArrayList<>();
        final int downEdgeOfPositions = getIndexOfMaterialsPart(block);
        final ArrayList<Line> lines = block.getLines();
        for (int i = 1; i < downEdgeOfPositions; i++) {
            final Line line = lines.get(i);
            if (line.getDescription().equals("ГОСТ 23279-2012")) {
                positionBars.addAll(parseRebarMesh(line));
            } else if (isPosition(line.getName())) {
                final PositionBar temp = parsePositionBar(line);
                positionBars.add(temp);
            }
        }
        return positionBars;
    }

    /**
     * Парсит арматурной сетку, возвращает список со стержнями.
     * @param line строка с арматурной сеткой.
     * @return список со стержнями.
     */
    private ArrayList<PositionBar> parseRebarMesh(Line line) {
        int meshQuantity = extractIntegerFrom(line.getQuantity());
        final String name = line.getName();
        final ArrayList<PositionBar> twoPositionsBars = getLengthOfTwoBars(name);
        getSignatureOfTwoBars(name, twoPositionsBars);
        return twoPositionsBars;
    }

    /**
     * Получает сигнатуру стержня из строки с сигнатурой арматурной сетки, обрабатывает и вносит данные в список стержней установленной длины.
     * @param name строка с сигнатурой арматурной сетки.
     * @param twoPositionsBars список стержней установленной длины.
     */
    private void getSignatureOfTwoBars(String name, ArrayList<PositionBar> twoPositionsBars) {
        final Pattern compile = Pattern.compile(REGEX_DIAMETER_AND_STEP_BARS_OF_REBAR_MESH);
        final Matcher matcher = compile.matcher(name);
        int i = matcher.groupCount();
        for (int j = 0; j < i; j++) {
            parseSignatureOfUnitBarAndCreatePositionBarFrom(matcher.group(j), twoPositionsBars, j);
        }
    }

    /**
     * Парсит сигнатуру стержней арматурной сетки. Получает диаметр, тип, количество стержней. Вносит данные в объекты списка PositionBar.
     * @param group строка с информацией о сигнатуре стержней арматурной сетки.
     * @param twoPositionsBars список с объектами стержней установленной длины.
     * @param numberOfBar порядкой номер стержня на обработке.
     */
    private void parseSignatureOfUnitBarAndCreatePositionBarFrom(String group, ArrayList<PositionBar> twoPositionsBars, int numberOfBar) {
        final char c = group.charAt(0);
        if (c > 57) {
            //секция для распаршивания ситуации, когда применяется проволока, а не стержень
            final Pattern compile = Pattern.compile(REGEX_FOR_INFO_ABOUT_WIRE_OF_REBAR_MESH);
            final Matcher matcher = compile.matcher(group);
            final int step = extractIntegerFrom(matcher.group(2));
            int quantityOfCurrentBar = getQuantityOfCurrentBarOfRebarMesh(step, numberOfBar, twoPositionsBars);
            String typeBar = matcher.group(1);
            int diameter = extractIntegerFrom(extractDiameterFromWire(matcher.group(0)));
            final PositionBar positionBar;
            if (numberOfBar == 0) {
                positionBar = twoPositionsBars.get(0);
            } else {
                positionBar = twoPositionsBars.get(1);
            }
            positionBar.setDiameter(diameter);
            positionBar.setRebarType(typeBar);
            positionBar.setQuantity(quantityOfCurrentBar);
            //секция для распаршивания ситуации, когда применяется проволока, а не стержень
        } else {

        }
    }

    /**
     * Возвращает диаметр стержня проволоки.
     * @param group строка с диаметром проволоки.
     * @return инт диаметр проволоки
     */
    private String extractDiameterFromWire(String group) {
        if (group.charAt(1) > 57) {
            return "" + group.charAt(2);
        }
        return "" + group.charAt(1);
    }

    /**
     * Получает количество текущих стержней в арматурной сетке по шагу и длине другого стержня.
     * @param step шаг стержней
     * @param numberOfBar порядкой номер стержня на обработке
     * @param twoPositionsBars список позиций с установленной длиной.
     * @return количество текущих стержней
     */
    private int getQuantityOfCurrentBarOfRebarMesh(int step, int numberOfBar, ArrayList<PositionBar> twoPositionsBars) {
        int quantityOfCurrentBar;
        if (numberOfBar == 0) {
            quantityOfCurrentBar = twoPositionsBars.get(1).getLength() / step;
        } else {
            quantityOfCurrentBar = twoPositionsBars.get(0).getLength() / step;
        }
        return quantityOfCurrentBar;
    }


    /**
     * Вычленяет описание длин стержней арматурной сетки. Передает строку обработчику и получает список из двух объектов, представляющих два стержня с установленными длинами. Возвращает этот список.
     *
     * @param name описание длин стержней арматурной сетки
     * @return список из двух объектов стержней с установленными длинами.
     */
    private ArrayList<PositionBar> getLengthOfTwoBars(String name) {
        final Pattern compile = Pattern.compile(REGEX_LENGTH_OF_BARS_OF_REBAR_MESH);
        final Matcher matcher = compile.matcher(name);
        final ArrayList<PositionBar> positionBars = new ArrayList<>();
        if (matcher.find()) {
            final String group = matcher.group();
            return getTwoPositionBarsWithSpecifiedLengthFrom(group);
        }
        return positionBars;
    }

    /**
     * Парсит из длины вида "65х140" две длины "65" и "140"(напр.), конвертирует в инт, переводит в мм, создает объект, устанавливает длину, запихивает объект в список и возвращает его.
     *
     * @param group строка, содержащая описание длин двух стержней арматурной сетки.
     * @return список двух стержневых позиций (объект стержня) с установленными длинами.
     */
    private ArrayList<PositionBar> getTwoPositionBarsWithSpecifiedLengthFrom(String group) {
        final Pattern compile = Pattern.compile(REGEX_FOR_UNIT_LENGTH_OF_BARS_OF_REBAR_MESH);
        final Matcher matcher = compile.matcher(group);
        final ArrayList<PositionBar> positionBars = new ArrayList<>();
        int i = matcher.groupCount();
        while (i > 0) {
            final PositionBar temp = new PositionBar();
            temp.setLength(extractIntegerFrom(matcher.group()) * 10); //cm -> mm
            positionBars.add(temp);
            i--;
        }
        return positionBars;
    }

    private int extractIntegerFrom(String quantity) {
        try {
            return Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            System.out.println("Зафакапил конвертацию из стринги в интегер, лоч");
        }
        return 0;
    }

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

}
