package entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

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
    }

    /**
     * ��������� �������� ����� ����� � ��������� �� ����� ���� �������� ���������� � ������������ ����� �������� �����
     * @param barType ���� �������� �����
     * @param diameterPositions ����� �������� � ����������-�������, ������-����������
     * @param index ���������� ����� ���������
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
     * @param name ��� ��������
     * @return ������ int
     */
    private int findIndexOf(String name) {
        return this.structureTitles.indexOf(name);
    }

    /**
     * ���������� ���������-��������� ������������� ����������� �������
     * @return
     */
    private String getContentTableString() {
        final StringBuilder table = new StringBuilder();
        table.append(getTitleBillTable());
        return null;
    }

    /**
     * ���������� ����� ���������
     * @return ������
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
