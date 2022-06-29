package service;

import entities.*;

import java.util.ArrayList;
import java.util.Collection;

public class CalculatedStructureCreator {

    private final ArrayList<CalculatedStructure> calculatedStructures = new ArrayList<>();

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
        final ArrayList<String> types = new ArrayList<>();
        final ArrayList<TypeBarBlock> typeBarBlocks = new ArrayList<>();
        types.addAll(createListOfBarTypes(structure));
        for (String type :
                types) {
            typeBarBlocks.add(createTypeBarBlockBy(type, structure));
        }
        return null;
    }

    /**
     * ������� TypeBarBlock �� ���� ��������� � ��������� ����� ��������
     * @param type ��������� ��� �������� (S500, S240, etc.)
     * @param structure ������ ���������
     * @return TypeBarBlock
     */
    private TypeBarBlock createTypeBarBlockBy(String type, Structure structure) {
        return null;
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
        positionBars.addAll(structure.getPositions());
        return positionBars;
    }
}
