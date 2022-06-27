package service;

import entities.PositionBar;
import entities.RebarMesh;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RebarMeshParser {
    public static void main(String[] args) {

//        final RebarMeshParser rebarMeshParser = new RebarMeshParser();
//        final RebarMesh build = rebarMeshParser.build("1С %%C12S500-200(300)/%%C8S500-600(500) 105x145 50/40", 4);
        String sample = "1С %%C12S500-200(300)/%%C8S500-600(500) 105x145 50/40";
        final Pattern compile = Pattern.compile(REGEX_SIGNATURE_REBAR_MESH);
        final Matcher matcher = compile.matcher(sample);
        System.out.println(matcher.matches());

    }

    private String content;

    /**
     * Парсит и создает объект арматурной сетки, состоящей из двух объектов PositionBar.
     *
     * @param signature сигнатура сетки
     * @param quantity  количество сеток по чертежу
     * @return объект арматурной сетки
     */
    public RebarMesh build(String signature, int quantity) {
        if (rebarMeshWithOutputsByDefault(signature)) {
            signature = signature.concat("25+25/25");
        }
        this.content = signature;
        if (signature.charAt(0) == '4' || signature.charAt(0) == '5') {
            // секция для легких сеток 4,5
            final Pattern compile = Pattern.compile(REGEX_SIGNATURE_REBAR_MESH);
            final Matcher matcher = compile.matcher(signature);
            if (matcher.matches()) {
                int diameterBase = extractIntegerFrom(matcher.group(1));
                String typeBarBase = matcher.group(2);
                int stepLengthwise = extractIntegerFrom(matcher.group(3));
                int stepLengthwiseExtra;
                if (matcher.group(4).equals("")) {
                    stepLengthwiseExtra = 0;
                } else {
                    stepLengthwiseExtra = extractIntegerFrom(matcher
                            .group(4)
                            .substring(1, matcher.group(4).length() - 1));
                }
                int diameterCross = extractIntegerFrom(matcher.group(5));
                String typeBarCross = matcher.group(6);
                int stepCrosswise = extractIntegerFrom(matcher.group(7));
                int stepCrosswiseExtra;
                if (matcher.group(8).equals("")) {
                    stepCrosswiseExtra = 0;
                } else {
                    stepCrosswiseExtra = extractIntegerFrom(matcher
                            .group(8)
                            .substring(1, matcher.group(8).length() - 1));
                }
                int lengthCrosswiseBar = extractIntegerFrom(matcher.group(9));
                int lengthLengthwiseBar = extractIntegerFrom(matcher.group(10));
                int outputCross1;
                int outputCross2;
                int outputLength1;
                if (matcher.group(12).equals("")) {
                    outputCross1 = outputCross2 = extractIntegerFrom(matcher.group(11));
                } else {
                    outputCross1 = extractIntegerFrom(matcher.group(11));
                    outputCross2 = extractIntegerFrom(matcher.group(12));
                }
                outputLength1 = extractIntegerFrom(matcher.group(13));

                final PositionBar base = new PositionBar();
                final PositionBar cross = new PositionBar();


                base.setDiameter(diameterBase);
                cross.setDiameter(diameterCross);

                base.setRebarType(typeBarBase);
                cross.setRebarType(typeBarCross);

                base.setLength(lengthLengthwiseBar * 10);
                cross.setLength(lengthCrosswiseBar * 10);

                base.setQuantity(getQuantityOfBarsByPair(stepLengthwise, stepLengthwiseExtra, lengthCrosswiseBar * 10, outputLength1, outputLength1) * quantity);
                cross.setQuantity(getQuantityOfBarsByPair(stepCrosswise, stepCrosswiseExtra, lengthLengthwiseBar * 10, outputCross1, outputCross2) * quantity);

                final RebarMesh rebarMesh = new RebarMesh();
                rebarMesh.setBase(base);
                rebarMesh.setCross(cross);
                return rebarMesh;
            }
        }
        return new RebarMesh();
    }

    /**
     * Подсчет количества продольных и поперечных стержней, исходя из сигнатуры сетки
     *
     * @param stepCurrent      шаг стержней, количество которых мы считаем.
     * @param stepCurrentExtra доборный шаг стержней, количество которых мы считаем.
     * @param lengthSubBar     длина стержней, на которых размещены стержни, которые мы считаем.
     * @param outputSubBar1    выпуск_1 стержней, на которых размещены стержни, которые мы считаем.
     * @param outputSubBar2    выпуск_2 стержней, на которых размещены стержни, которые мы считаем.
     * @return int количество стержней, которые мы считали.
     */
    private int getQuantityOfBarsByPair(int stepCurrent, int stepCurrentExtra, int lengthSubBar, int outputSubBar1, int outputSubBar2) {
        int quantityCurrentBar;
        quantityCurrentBar = ((lengthSubBar - outputSubBar1 - outputSubBar2) - stepCurrentExtra) / stepCurrent + 1;
        if (stepCurrentExtra > 0) {
            return quantityCurrentBar + 1;
        } else {
            return quantityCurrentBar;
        }
    }

    /**
     * Возвращает true, если в сигнатуре сетки не указаны выпуски ввиду того, что их считают по умолчанию (согласно госту 25мм).
     *
     * @param content сигнатура сетки
     * @return true или false
     */
    private boolean rebarMeshWithOutputsByDefault(String content) {
        final Pattern compile = Pattern.compile(REGEX_CHECK_REBAR_OUTPUTS);
        final Matcher matcher = compile.matcher(content);
        return !matcher.find();
    }

    /**
     * Парсит строку в инт, если невозможно, то возвращает ноль.
     *
     * @param s строка, которую нужно преобразовать в инт.
     * @return число
     */
    private int extractIntegerFrom(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Зафакапил конвертацию из стринги в интегер, лоч");
        }
        return 0;
    }

    public RebarMeshParser(String content) {
        this.content = content;
    }

    public RebarMeshParser() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    /**
     * Проверяет указаны ли в сигнатуре сетки выпуски
     */
    public final static String REGEX_CHECK_REBAR_OUTPUTS = "\\/[0-9]{2}";
    /**
     * <h2>INFO:</h2>
     * https://regex101.com/r/jHXJDB/1
     * <p></p>
     * <h2>Samples:</h2>
     * <ul>
     *     <li>4С %%C5S500-100(50)/%%C5S500-100 75x150 50+50/25</li>
     *     <li>4С %%C5S500-100/%%C5S500-100(50) 75x150 50/25</li>
     *     <li>Тоже самое справедливо для тяжелых сеток</li>
     * </ul>
     * Группы, которые мы полчаем:
     * <ol>
     *     <li>group: %%C</li>
     *     <li>group: 5</li>
     *     <li>group: S500</li>
     *     <li>group: 100</li>
     *     <li>group: (50)</li>
     *     <li>group: %%C</li>
     *     <li>group: 5</li>
     *     <li>group: S500</li>
     *     <li>group: 100</li>
     *     <li>group: null</li>
     *     <li>group: 75</li>
     *     <li>group: 150</li>
     *     <li>group: 50</li>
     *     <li>group: +50</li>
     *     <li>group: 25</li>
     * </ol>
     * <ol>
     *     <li>group: %%C</li>
     *     <li>group: 5</li>
     *     <li>group: S500</li>
     *     <li>group: 100</li>
     *     <li>group: </li>
     *     <li>group: %%C</li>
     *     <li>group: 5</li>
     *     <li>group: S500</li>
     *     <li>group: 100</li>
     *     <li>group: (50)</li>
     *     <li>group: 75</li>
     *     <li>group: 150</li>
     *     <li>group: 50</li>
     *     <li>group: </li>
     *     <li>group: 25</li>
     * </ol>
     */
    public final static String REGEX_SIGNATURE_REBAR_MESH = "[0-9][C,c,С,с][ ]+%%[C,c,С,с]([0-9]{1,2})([S,A,А][0-9]{3})-([0-9]{3})(\\([0-9]{2,3}\\)|)\\/%%[C,c,С,с]([0-9]{1,2})([S,A,А][0-9]{3})-([0-9]{3})(\\([0-9]{2,3}\\)|)[ ]+([0-9]{2,3})[X,x,х,Х]([0-9]{2,3})[ ]+([0-9]{2,3})(\\+[0-9]{2,3}|)\\/([0-9]{2,3})";

}
