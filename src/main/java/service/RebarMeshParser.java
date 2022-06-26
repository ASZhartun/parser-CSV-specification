package service;

import entities.PositionBar;
import entities.RebarMesh;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RebarMeshParser {
    public static void main(String[] args) {

        final RebarMeshParser rebarMeshParser = new RebarMeshParser();
        final RebarMesh build = rebarMeshParser.build("4� %%C5S500-100/%%C5S500-100 75x150 50+50/25", 4);
        System.out.println();

    }

    private String content;

    /**
     * ������ � ������� ������ ���������� �����, ��������� �� ���� �������� PositionBar.
     * @param signature ��������� �����
     * @param quantity ���������� ����� �� �������
     * @return ������ ���������� �����
     */
    public RebarMesh build(String signature, int quantity) {
        this.content = signature;
        if (signature.charAt(0) == '4' || signature.charAt(0) == '5') {
            // ������ ��� ������ ����� 4,5
            if (wireRebarMeshWithOutputsByDefault(signature)) {
                signature = signature.concat("25+25/25");
            }
            final Pattern compile = Pattern.compile(REGEX_SIGNATURE_WIRE_REBAR_MESH_BY_GROUPS);
            final Matcher matcher = compile.matcher(signature);
            if (matcher.find()) {
                int diameterBase = extractIntegerFrom(matcher.group(2));
                String typeBarBase = matcher.group(3);
                int stepLengthwise = extractIntegerFrom(matcher.group(4));
                int stepLengthwiseExtra;
                if (matcher.group(5).equals("")) {
                    stepLengthwiseExtra = 0;
                } else {
                    stepLengthwiseExtra = extractIntegerFrom(matcher
                            .group(5)
                            .substring(1, matcher.group(5).length() - 1));
                }
                int diameterCross = extractIntegerFrom(matcher.group(7));
                String typeBarCross = matcher.group(8);
                int stepCrosswise = extractIntegerFrom(matcher.group(9));
                int stepCrosswiseExtra;
                if (matcher.group(10).equals("")) {
                    stepCrosswiseExtra = 0;
                } else {
                    stepCrosswiseExtra = extractIntegerFrom(matcher
                            .group(10)
                            .substring(1, matcher.group(10).length() - 1));
                }
                int lengthCrosswiseBar = extractIntegerFrom(matcher.group(11));
                int lengthLengthwiseBar = extractIntegerFrom(matcher.group(12));
                int outputCross1;
                int outputCross2;
                int outputLength1;
                if (matcher.group(14).equals("")) {
                    outputCross1 = outputCross2 = extractIntegerFrom(matcher.group(13));
                } else {
                    outputCross1 = extractIntegerFrom(matcher.group(13));
                    outputCross2 = extractIntegerFrom(matcher.group(14));
                }
                outputLength1 = extractIntegerFrom(matcher.group(15));

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
        } else {

            // ������ ��� ������� ����� ���: 1-3
        }
        return null;
    }

    /**
     * ������� ���������� ���������� � ���������� ��������, ������ �� ��������� �����
     * @param stepCurrent ��� ��������, ���������� ������� �� �������.
     * @param stepCurrentExtra �������� ��� ��������, ���������� ������� �� �������.
     * @param lengthSubBar ����� ��������, �� ������� ��������� �������, ������� �� �������.
     * @param outputSubBar1 ������_1 ��������, �� ������� ��������� �������, ������� �� �������.
     * @param outputSubBar2 ������_2 ��������, �� ������� ��������� �������, ������� �� �������.
     * @return int ���������� ��������, ������� �� �������.
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
     * ���������� true, ���� � ��������� ����� �� ������� ������� ����� ����, ��� �� ������� �� ��������� (�������� ����� 25��).
     * @param content ��������� �����
     * @return true ��� false
     */
    private boolean wireRebarMeshWithOutputsByDefault(String content) {
        final Pattern compile = Pattern.compile(REGEX_CHECK_WIRE_REBAR_OUTPUTS);
        final Matcher matcher = compile.matcher(content);
        return !matcher.find();
    }

    /**
     * ������ ������ � ���, ���� ����������, �� ���������� ����.
     * @param s ������, ������� ����� ������������� � ���.
     * @return �����
     */
    private int extractIntegerFrom(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("��������� ����������� �� ������� � �������, ���");
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
     * <h2>INFO:</h2>
     * https://regex101.com/r/jHXJDB/1
     * <p></p>
     * <h2>Samples:</h2>
     * <ul>
     *     <li>4� %%C5S500-100(50)/%%C5S500-100 75x150 50+50/25</li>
     *     <li>4� %%C5S500-100/%%C5S500-100(50) 75x150 50/25</li>
     * </ul>
     * ������, ������� �� �������:
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
    public final static String REGEX_SIGNATURE_WIRE_REBAR_MESH_BY_GROUPS = "(%%[C,c])([0-9]{1,2})(S[0-9]{3})-([0-9]{2,3})(\\([0-9]{2,3}\\)|)\\/(%%[C,c])([0-9]{1,2})(S[0-9]{3})-([0-9]{2,3})(\\([0-9]{2,3}\\)|)[ ]+([0-9]{2,3})[x,X,�,�]([0-9]{2,3})[ ]([0-9]{2,3})([+][0-9]{2,3}|)\\/([0-9]{2,3})";
    /**
     * ��������� ������� �� � ��������� ����� �������
     */
    public final static String REGEX_CHECK_WIRE_REBAR_OUTPUTS = "\\/[0-9]{2}";

}
