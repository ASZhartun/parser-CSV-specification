package service.base;

import entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatedStructureCreator {
    /**
     * ������ ����������� ��������.
     */
    private final ArrayList<CalculatedStructure> calculatedStructures = new ArrayList<>();

    public CalculatedStructureCreator() {

    }

    /**
     * ������� �� �������� ������������ ���������, � ������� ���� ��������� �������� �� ���� ����������� ��� �������
     * ��������.
     *
     * @param structures ��������� ��� ��������.
     */
    public void build(ArrayList<Structure> structures) {
        for (Structure structure :
                structures) {
            calculatedStructures.add(calculate(structure));
        }
    }

    /**
     * ������� ������������ ��������� �� ������ ���������.
     *
     * @param structure ������ ���������
     * @return ������������ ��������� CalculatedStructure
     */
    private CalculatedStructure calculate(Structure structure) {
        final CalculatedStructure calculatedStructure = new CalculatedStructure();
        final ArrayList<TypeBarBlock> typeBarBlocks = createTypeBarBlockFrom(structure);
        calculatedStructure.setTypeBarBlocks(typeBarBlocks);
        calculatedStructure.setTitle(structure.getTitle());
        calculatedStructure.calculateTotalWeight();
        return calculatedStructure;
    }

    /**
     * ������� ������ ������ � ������������� ������ �������� (S240,S500, etc.)
     *
     * @param structure ������ ���������
     * @return ������ �������� ������, � ���������� ������ ��������
     */
    public ArrayList<TypeBarBlock> createTypeBarBlockFrom(Structure structure) {
        final ArrayList<TypeBarBlock> typeBarBlocks = new ArrayList<>();
        final ArrayList<String> types = new ArrayList<>(createListOfBarTypes(structure));
        for (String type :
                types) {
            typeBarBlocks.add(createTypeBarBlockBy(type, structure));
        }
        return typeBarBlocks;
    }

    /**
     * ������� TypeBarBlock �� ���� ��������� � ��������� ����� ��������
     *
     * @param type      ��������� ��� �������� (S500, S240, etc.)
     * @param structure ������ ���������
     * @return TypeBarBlock
     */
    private TypeBarBlock createTypeBarBlockBy(String type, Structure structure) {
        final ArrayList<PositionBar> allPositionsBar = getAllPositionsBarFrom(structure);
        final List<PositionBar> collect = allPositionsBar
                .stream()
                .filter((item) -> item.getRebarType().equals(type))
                .collect(Collectors.toList());
        final TypeBarBlock typeBarBlock = new TypeBarBlock();
        typeBarBlock.setBarType(type);
        typeBarBlock.setDiameterPositionWeights(createBarMapDiameterFrom(new ArrayList<>(collect)));
        typeBarBlock.calculateTotalWeight();
        return typeBarBlock;
    }

    /**
     * ��������� ���� �� ������ PositionBar. ���� - �������.
     *
     * @param allPositionsBar ������ PositionBar.
     * @return HashMap<Integer, Double>
     */
    private HashMap<Integer, Double> createBarMapDiameterFrom(ArrayList<PositionBar> allPositionsBar) {
        final HashMap<Integer, Double> map = new HashMap<>();
        allPositionsBar
                .forEach((item) -> map.merge(item.getDiameter(), item.getWeight() * item.getQuantity(), Double::sum));
        return map;
    }

    /**
     * ���������� ������ ������������ ����� �������� � ���������.
     *
     * @param structure ������ ���������
     * @return ������ ����� � ������ ��������, ������� ���� � ���������.
     */
    private Collection<String> createListOfBarTypes(Structure structure) {
        final ArrayList<PositionBar> positionBars = getAllPositionsBarFrom(structure);
        final ArrayList<String> types = new ArrayList<>();
        for (PositionBar positionBar :
                positionBars) {
            if (!types.contains(positionBar.getRebarType())) {
                types.add(positionBar.getRebarType());
            }
        }
        return types;
    }

    /**
     * ���������� ������ ������ PositionBar �� ������ ���������� ������� � ������ ���������� �����.
     *
     * @param structure ������ ���������
     * @return ������ ������ �������
     */
    private ArrayList<PositionBar> getAllPositionsBarFrom(Structure structure) {
        final ArrayList<PositionBar> positionBars = new ArrayList<>();
        for (RebarMesh rebarMesh :
                structure.getRebarMeshes()) {
            positionBars.add(rebarMesh.getBase());
            positionBars.add(rebarMesh.getCross());
        }

        for (RebarCage rebarCage : structure.getRebarCages()) {
            positionBars.addAll(rebarCage.getBars());
        }
        positionBars.addAll(structure.getPositions());
        return positionBars;
    }

    public ArrayList<CalculatedStructure> getCalculatedStructures() {
        return calculatedStructures;
    }
}
