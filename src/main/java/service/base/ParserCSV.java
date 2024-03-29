package service.base;

import entities.*;
import exceptions.CreatingTableException;
import org.springframework.beans.factory.annotation.Autowired;
import service.extra.Librarian;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParserCSV {
    private Librarian librarian;

    /**
     * ��������������� ������ ���������� ����� �� ���� 23279-2012.
     */

    private RebarMeshParser rebarMeshParser;
    /**
     * ������, ���������� ��� ���������, ������� ������� � ����� �������.
     */
    private ArrayList<Structure> structures = new ArrayList<>();
    /**
     * <p>������� ����, ������� ������������� regex'�:</p>
     * <ul>
     *     <li><pre>16 S500 ��� 1704-2012, L=5999</pre></li>
     *     <li><pre>8  S500   ��� 1704-2012,L=5999</pre></li>
     *     <li><pre>8    S240   ��� 1704-2012, L=600</pre></li>
     *     <li><pre>8    S240   ���1704-2012, L=350</pre></li>
     * </ul>
     */
    private static final String REGEX_POSITION_NAME_WITH_LENGTH = "[0-9]{1,2}[ ]+[S,A,�][0-9]{3}[ ]+���[ ]?1704-2012,[ ]?[L](.+)?[=][0-9]+";


    public ParserCSV() {

    }

    /**
     * <h2>�������� ������ �������.</h2>
     * <h3>������� ������</h3>
     * <ol>
     *     <li>��������� ������ ����� ������� �� ������, ������� �������� ��� ������ CSV-�����.</li>
     *     <li>����� ������� ��������� �� ������� ������� ��������.</li>
     *     <li>������ ������� ��������� ������ � ������� ������ ���������, ������� ������ � ������ �������� ���
     *      ���������� ������.</li>
     * </ol>
     *
     * @param content ������, �������� �� csv �����.
     */
    public void parseTable(ArrayList<String> content) {
        BlockTable blockTable = formTable(content);
        ArrayList<BlockTable> structureBlocks = getBlockStructureListFromCommonTable(blockTable);
        loopParseStructureBlocksData(structureBlocks);
    }

    /**
     * ���������� ������ � ������ �������. ������� ����� ������� �����
     *
     * @param content ��������� �����, ���������� �� csv �����
     * @return ������ �������
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
                System.out.println("������������ ������� � ����� ������-��");
            } catch (Exception e) {
                System.out.println("����� �� ����� ���������� � � ������������� �������, ���");
            }
        }
        return blockTable;
    }

    /**
     * ��������� ������ ������, ������ �� ������� ����� �� ����� ������������ ���������.
     *
     * @param blockTable ����� �������
     * @return ������ ������� ������
     */
    private ArrayList<BlockTable> getBlockStructureListFromCommonTable(BlockTable blockTable) {
        final ArrayList<BlockTable> tables = new ArrayList<>();
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
                    tables.add(temp);
                    upBorderIndex = commonLines.indexOf(line);
                }
            }
        }
        return tables;

    }

    /**
     * ��������� ���� �� � ������ ��������� ��������(���������).
     *
     * @param line ������ �������
     * @return ��, ���� �����.
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
     * ������� ������� �������
     *
     * @param name
     * @return ������ ��� �������
     */
    private String deleteQuotes(String name) {
        return name.substring(1, name.length() - 1);
    }

    /**
     * ��������� ���������� �� ������ � ������� (�������� �� ������ � �������)
     *
     * @param line ������ �������
     * @return true/false
     */
    private boolean titleIsWrappedQuotes(Line line) {
        return line.getName().indexOf("\"") == 0;
    }

    /**
     * ��������� ���������� �� ���� "������������" � ������ � ������� ������� �����.
     *
     * @param line ����������� ������
     * @return ��, ���� ��
     */
    private boolean startedWithBigLetter(Line line) {
        int numericValueOfFirstLetter = line.getName().charAt(0);
        return numericValueOfFirstLetter > 1039 && numericValueOfFirstLetter < 1072;
    }

    /**
     * ��������� �������� �� ���� ������� �������� �� �������� �������� �������.
     *
     * @param line ����������� ������
     * @return ��, ���� �������� ��������
     */
    private boolean isNotStandardTitles(Line line) {
        return !line.getName().equals("���������") &&
                !line.getName().equals("��������� �������") &&
                !line.getName().equals("������") &&
                !line.getName().equals("������� ���������") &&
                !line.getName().equals("�����");
    }

    /**
     * ��������� ������ �� ���������� ������ ���� "������������"
     *
     * @param line ����������� ������
     * @return ��, ���� ��������� <b>������</b> ���� "������������"
     */
    private boolean isEmptyAroundPotentialStructureName(Line line) {
        return line.getPos().equals(" ") && line.getDescription().equals(" ")
                && line.getQuantity().equals(" ") && line.getNote().equals(" ");
    }

    /**
     * ��������� ������������ ��������� � ������ ��������, ������������ � ����� �������.
     *
     * @param structureBlocksData ������ ������� ������, ���������� �������� ��������.
     */
    private void loopParseStructureBlocksData(ArrayList<BlockTable> structureBlocksData) {
        for (BlockTable block :
                structureBlocksData) {
            structures.add(parseCurrentStructureBlock(block));
        }
    }

    /**
     * ��������� �� ������� �� ���������� ������ ���������(��������).
     *
     * @param block ������� � ������� ���������
     * @return ���������
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
     * ���������� ������ ����� �� ����� ���������, ���������� �������� ����� ������
     *
     * @param block ����, ����������� ���������
     * @return ������ �����, ����������� �����
     */
    private ArrayList<String> parseBlockConcreteDefinition(BlockTable block) {
        final ArrayList<String> concreteDefinedLines = new ArrayList<>();
        int indexConcrete = getIndexOfMaterialsPart(block) + 1;
        final ArrayList<Line> lines = block.getLines();
        for (int i = indexConcrete; i < lines.size(); i++) {
            concreteDefinedLines.add(lines.get(i).toString());
        }
        return concreteDefinedLines;
    }

    /**
     * ��������� �� �������� � ��������� �������, ������� �������� ��� ������� ��� ��� ����� �� ����.
     * ������ � ����������� �� ����, ��� ���. ��������� ��� ������� �������. ��������� �� � ������.
     * ������ ������������.
     *
     * @param block ������� ���������.
     */
    private void parseBlockBarsPositions(BlockTable block, Structure structure) {
        removeWrappingQuotes(block);
        final ArrayList<PositionBar> positionBars = new ArrayList<>();
        final ArrayList<RebarMesh> rebarMeshes = new ArrayList<>();
        final int downEdgeOfPositions = getIndexOfMaterialsPart(block);
        final ArrayList<Line> lines = block.getLines();
        for (int i = 0; i < downEdgeOfPositions; i++) {
            final Line line = lines.get(i);
            if (line.getDescription().equals("���� 23279-2012")) {
                final RebarMesh rebarMesh = rebarMeshParser.build(line.getName(), line.getQuantity());
                structure.getRebarMeshes().add(rebarMesh);
            } else if (existsAtLibrary(line.getName())) {
                final RebarCage item = librarian.getItemBy(line.getName());
                RebarCage instance = new RebarCage();
                try {
                    instance = item.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                instance.setQuantity(Integer.parseInt(line.getQuantity()));
                instance.setDoc(line.getDescription());
                structure.getRebarCages().add(instance);
            } else if (isPosition(line.getName())) {
                final PositionBar temp = parsePositionBar(line);
                structure.getPositions().add(temp);
            }
        }
        System.out.println();
    }

    /**
     * ��������� ������������� ����������������� ������� � ���������� �� ��������� �����
     *
     * @param name ��� �������
     * @return true/false
     */
    private boolean existsAtLibrary(String name) {
        final RebarCage rebarCage = new RebarCage();
        rebarCage.setTitle(name);
        return librarian.getExtraUnitStorage().getExtraUnits().contains(rebarCage);
    }

    /**
     * ������� �������, ������� ����������� �������� � ������� ��� ������ �� csv ����� ����������� �� ������ ������ �������.
     *
     * @param block ���� �� ����������
     */
    private void removeWrappingQuotes(BlockTable block) {
        block.getLines().forEach((item) -> {
            item.setPos(item.getPos().replaceAll("\"", ""));
            item.setDescription(item.getDescription().replaceAll("\"", ""));
            item.setName(item.getName().replaceAll("\"", ""));
            item.setQuantity(item.getQuantity().replaceAll("\"", ""));
            item.setWeight(item.getWeight().replaceAll("\"", ""));
            item.setNote(item.getNote().replaceAll("\"", ""));
        });
    }


    /**
     * ������ ������ � ��������� �������, ������� � ���������� ������ ���������� �������.
     *
     * @param line ������ ������, � ������� ��������� ������� � ���������� ��������.
     * @return ������ ������� �������
     */
    private PositionBar parsePositionBar(Line line) {
        final PositionBar positionBar = new PositionBar();
        positionBar.setQuantity(getQuantityAndParseToInt(line));
        setDiameterAndTypeAndLengthTo(positionBar, line);
        return positionBar;
    }

    /**
     * ������������� �������, ��� ��������, ����� � �������.
     *
     * @param positionBar �������, � ������� ������������� ��������
     * @param line        ������, ���������� ������ �� �������
     */
    private void setDiameterAndTypeAndLengthTo(PositionBar positionBar, Line line) {
        String name = line.getName();
        name = name.trim();
        final String[] split = name.split("[ ]+");
        positionBar.setDiameter(extractDiameter(split[0]));
        positionBar.setRebarType(split[1]);
        positionBar.setLength(extractLengthFrom(split[4]));
    }

    /**
     * ��������� ������� �� ������ � ������������ � int.
     *
     * @param s ������ � ���������
     * @return ������� ���� int
     */
    private int extractDiameter(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("������� ���������� ��� ���������� �� \"������������\"");
        }
        return 0;
    }

    /**
     * ��������� ����� �� ������ "L=9999": int x = 9999;
     *
     * @param s ������ � ������
     * @return ����� ���� int
     */
    private int extractLengthFrom(String s) {
        try {
            return Integer.parseInt(s.split("=")[1]);
        } catch (NumberFormatException e) {
            System.out.println("����� ����������� ��� ���������� �� \"������������\"");
        }
        return 0;
    }

    /**
     * ���� � ������ ������ "����������". �������� �������� ������ � ������������ � int. ���������� ���������� ��������.
     *
     * @param line ������ ������, ����������� �������.
     * @return int ���������� �������.
     */
    private int getQuantityAndParseToInt(Line line) {
        try {
            return Integer.parseInt(line.getQuantity());
        } catch (NumberFormatException e) {
            System.out.println("���������� �������� �� ���������� � �������");
        }
        return 0;
    }

    /**
     * ���������, ��� ������������ ������������� ������� �������.
     *
     * @param name ������� ���� "������������"
     * @return true, ���� �������������
     */
    private boolean isPosition(String name) {
        final Pattern compile = Pattern.compile(REGEX_POSITION_NAME_WITH_LENGTH);
        final Matcher matcher = compile.matcher(name);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * ������� ������ ������ "���������". ������� �������� ������ ������. ������������ � double � ������.
     *
     * @param block ������� ���������
     * @return ������ ������ ���� double
     */
    private Double parseBlockConcreteVolume(BlockTable block) {
        int indexConcrete = getIndexOfMaterialsPart(block) + 1;
        String concreteValue = block.getLines().get(indexConcrete).getNote();
        return extractDoubleValueOfConcreteVolumeFrom(concreteValue);
    }

    /**
     * ������� �� ������ ����� ������ ���� double.
     *
     * @param concreteValue ������ � ������� ������. ����., "2,3�3".
     * @return ����� ������ ���� double
     */
    private Double extractDoubleValueOfConcreteVolumeFrom(String concreteValue) {
        String pattern = "[0-9]+,[0-9]{1,2}";
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
     * ���������� ������ ������ ������ "���������"
     *
     * @param block ������� ���������
     * @return ������ ���� int
     */
    private int getIndexOfMaterialsPart(BlockTable block) {
        final ArrayList<Line> lines = block.getLines();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).getName().equals("���������")) return i;
        }
        return 0;
    }

    /**
     * �������� ��������� ����� �� ������������ ��������� (������������)
     *
     * @param block ������� ���������
     * @return ��������� ����� �� ������������
     */
    private String parseBlockTitle(BlockTable block) {
        if (!block.getLines().get(0).getPos().equals(" ")) {
            return " ";
        }
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

    public Librarian getLibrarian() {
        return librarian;
    }

    @Autowired
    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
}
