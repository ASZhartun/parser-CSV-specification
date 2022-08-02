package service.base;

import entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatedStructureCreator {
    /**
     * —писок обсчитанных структур.
     */
    private final ArrayList<CalculatedStructure> calculatedStructures = new ArrayList<>();

    public CalculatedStructureCreator() {

    }

    /**
     * —оздает из структур ѕросчитанные —труктуры, в которой есть суммарные значени€ по типу армировани€ дл€ каждого
     * диаметра.
     *
     * @param structures структуры дл€ просчета.
     */
    public void build(ArrayList<Structure> structures) {
        for (Structure structure :
                structures) {
            calculatedStructures.add(calculate(structure));
        }
    }

    /**
     * —оздает ѕросчитанную структуру из данной структуры.
     *
     * @param structure данна€ структура
     * @return просчитанна€ структуру CalculatedStructure
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
     * —оздает список блоков с определенными типами арматуры (S240,S500, etc.)
     *
     * @param structure данна€ структура
     * @return список объектов блоков, с указанными типами арматуры
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
     * —оздает TypeBarBlock на базе структуры с указанным типом арматуры
     *
     * @param type      указанный тип арматуры (S500, S240, etc.)
     * @param structure данна€ структура
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
     * ‘ормирует мапу из списка PositionBar.  люч - диаметр.
     *
     * @param allPositionsBar список PositionBar.
     * @return HashMap<Integer, Double>
     */
    private HashMap<Integer, Double> createBarMapDiameterFrom(ArrayList<PositionBar> allPositionsBar) {
        final HashMap<Integer, Double> map = new HashMap<>();
        allPositionsBar
                .forEach((item) -> map.merge(item.getDiameter(), item.getWeight() * item.getQuantity(), Double::sum));
        return map;
    }

    /**
     * ¬озвращает список существующих типов арматуры в структуре.
     *
     * @param structure данна€ структура
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
     * ¬озвращает полный список PositionBar из списка стержневых позиций и списка арматурных сеток.
     *
     * @param structure данна€ структура
     * @return полный список позиций
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
