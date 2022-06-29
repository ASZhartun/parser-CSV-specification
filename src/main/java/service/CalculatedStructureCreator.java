package service;

import entities.*;

import java.util.ArrayList;
import java.util.Collection;

public class CalculatedStructureCreator {

    private final ArrayList<CalculatedStructure> calculatedStructures = new ArrayList<>();

    /**
     * Создает из структур Просчитанные Структуры, в которой есть суммарные значения по типу армирования для каждого
     * диаметра.
     *
     * @param structures структуры для просчета.
     */
    public void build(ArrayList<Structure> structures) {
        for (Structure structure :
                structures) {
            calculatedStructures.add(calculate(structure));
        }
    }

    /**
     * Создает Просчитанную структуру из данной структуры.
     *
     * @param structure данная структура
     * @return просчитанная структуру CalculatedStructure
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
     * Создает TypeBarBlock на базе структуры с указанным типом арматуры
     * @param type указанный тип арматуры (S500, S240, etc.)
     * @param structure данная структура
     * @return TypeBarBlock
     */
    private TypeBarBlock createTypeBarBlockBy(String type, Structure structure) {
        return null;
    }

    /**
     * Возвращает список существующих типов арматуры в структуре.
     *
     * @param structure данная структура
     * @return список строк с типами арматуры, которые есть в структуре.
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
     * Возвращает полный список PositionBar из списка стержневых позиций и списка арматурных сеток.
     * @param structure данная структура
     * @return полный список позиций
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
