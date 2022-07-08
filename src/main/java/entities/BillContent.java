package entities;

import java.util.*;

/**
 * ������ ���� � ���� �������� � ��������� ������� �����.
 */
public class BillContent {
    /**
     * ������ ���������� ������������ ��������.
     */
    public ArrayList<String> structureTitles = new ArrayList<>();
    /**
     * ��������� ������ ������������ �����(���������) �������� (����. S240, S500, etc.)
     */
    public TreeSet<TypeBarBillBlock> billBlocks = new TreeSet<>(Comparator.comparing(TypeBarBillBlock::getBarType));
    /**
     * ������� ������������ ���������, �� ������� ��������� ������ �������� ��������� ������� �����.
     */
    public ArrayList<CalculatedStructure> calculatedStructures;

    /**
     * ������������ ���� � ���������� ������������ �������� � ���������� ���� � ���������� ������������ ���� �������� � �������� ����������.
     *
     * @param calculatedStructures ������ ������������ ��������
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
     * ������ ����� �� ������� �������
     */
    private void cleanBlocks() {
        billBlocks.forEach(this::clean);
    }

    /**
     * ������ ���� �� ������� �������
     *
     * @param item ���� � ���������� � ������ �� ���� ����������
     */
    private void clean(TypeBarBillBlock item) {
        item.getDiameterPosition().entrySet().removeIf(this::hasAllZeroValues);
    }

    /**
     * ���������� true, ���� ��� �������� ������ �������� �� �������� ����� 0.
     *
     * @param next ���� ���� � ��������� � ������� ��������.
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
     * ���������� �������� ���� �������� ������������ �������� � ����� ����� ������� �������� ���������.
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
     * ��������� �� ���� ������ ����� ������� ���� � �����.
     */
    private void roundBlockWeightValues() {
        billBlocks.forEach(this::round);
    }

    /**
     * ��������� ��� �������� �������� �� ���� ������ ����� �������.
     *
     * @param typeBarBillBlock ���� ���������� �������� � �� ���� ��� ���� �������.
     */
    private void round(TypeBarBillBlock typeBarBillBlock) {
        typeBarBillBlock.getDiameterPosition().forEach((key, value) -> value.forEach((weight) ->
                weight = Double.parseDouble(String.format("%.2f", weight).replaceAll(",", "."))
        ));
    }

    /**
     * ��������� �������� ����� ����� � ��������� �� ����� ���� �������� ���������� � ������������ ����� �������� �����
     *
     * @param barType           ���� �������� �����
     * @param diameterPositions ����� �������� � ����������-�������, ������-����������
     * @param index             ���������� ����� ���������
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
     * ���� ���������� ����� ��������� � ������ ��������� �� ������� ������������
     *
     * @param name ��� ��������
     * @return ������ int
     */
    private int findIndexOf(String name) {
        return this.structureTitles.indexOf(name);
    }

    /**
     * ���������� ��������� ������������� ���������.
     *
     * @return ������
     */
    public String getContentTableString() {
        //������� � ������ ��������� � ������� "�����" �� ���������
        int columns = 2;
        //���������� ������� ���������� ��� �������� ������� �����
        columns += billBlocks.stream().mapToInt((item) -> item.getDiameterPosition().size()).sum();
        //������� ��� "�����" �� ������� �����
        columns += billBlocks.size();
        //������� "�����"
        columns++;
        //���������� ������� ��������� ��� ������� �����
        final ArrayList<Integer> blockSizes = new ArrayList<>();
        billBlocks.forEach((item) -> blockSizes.add(item.getDiameterPosition().size()));
        return createStaticTitle(columns) +
                createBlocksTitle(blockSizes, columns) +
                createContent(blockSizes, columns);
    }

    /**
     * ������� ������ ��� ������ ��������� (������������).
     *
     * @param columns ���������� ������� ���������
     * @return ��������� ������������� ������ ���� ����� �������
     */
    private String createStaticTitle(int columns) {
        final StringBuilder staticHead = new StringBuilder();
        for (int i = 0; i < billBlocks.size(); i++) {
            if (i == 0) {
                staticHead.append("����� ��������;").append("������� ����������");
                staticHead.append(addEmptyCellsWith(columns - 3, " ;"));
                staticHead.append("�����;\n");
            }
            if (i == 1) {
                staticHead.append(" ;");
                staticHead.append("�������� ������");
                staticHead.append(addEmptyCellsWith(columns - 2, " ;"));
                staticHead.append('\n');
            }
        }
        return staticHead.toString();
    }

    /**
     * ���������� ������ � ����������� ����������� � ��������� � ������� ���������.
     *
     * @param blockSizes ������ � ��������� ���������� ������
     * @param columns    ����� ������� ���������
     * @return ������-������������� ������ ��������� ������� �����
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
                            .append("��� 1704-2012")
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
     * ��������� ��������� ��������� � ����� ���������.
     * @return ������-������������� ��������� ���������
     */
    private String addDiameterTitles() {
        final StringBuilder diameters = new StringBuilder();
        diameters.append(";");
        billBlocks.forEach((block) -> {
            block.getDiameterPosition().keySet().forEach((key) -> {
                diameters.append(key).append(";");
            });
            diameters.append("�����;");
        });
        diameters.append(" ;\n");
        return diameters.toString();
    }

    /**
     * ���������� ������ ������� � ����������, ���������� ����� ���� ��������.
     *
     * @param blockSizes ������ �������� ���������� ������
     * @param columns    ����� ������� ���������
     * @return ������-������������� ������ �� ���� ����������
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
     * ���������� ��������� �������� ����� �������� ��������� �������� ����������� ����� � ���� ������.
     *
     * @param item  ���������� ����
     * @param index ���������� ����� ��������� ���������
     * @return ������ ���������� ����
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
     * ���������� ��������� ������������� ����� �� ���� ��������� ��� ������� ���������
     * � �������� �������� �����.
     *
     * @param item  ���������� ���� ���������
     * @param index ���������� ����� ��������� � ���������
     * @return ������
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
     * ��������� ������ � ������������ �������� CSV �������
     *
     * @param i ���������� �����
     * @param s ������� ����������
     * @return ������
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
