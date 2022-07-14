package service.base;

import entities.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatedStructureCreator {
    /**
     * Список обсчитанных структур.
     */
    private final ArrayList<CalculatedStructure> calculatedStructures = new ArrayList<>();

    public CalculatedStructureCreator() {

    }

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
        final ArrayList<TypeBarBlock> typeBarBlocks = new ArrayList<>();
        final ArrayList<String> types = new ArrayList<>(createListOfBarTypes(structure));
        for (String type :
                types) {
            typeBarBlocks.add(createTypeBarBlockBy(type, structure));
        }
        calculatedStructure.setTypeBarBlocks(typeBarBlocks);
        calculatedStructure.setTitle(structure.getTitle());
        calculatedStructure.calculateTotalWeight();
        return calculatedStructure;
    }

    /**
     * Создает TypeBarBlock на базе структуры с указанным типом арматуры
     *
     * @param type      указанный тип арматуры (S500, S240, etc.)
     * @param structure данная структура
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
     * Формирует мапу из списка PositionBar. Ключ - диаметр.
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
     *
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

    public ArrayList<CalculatedStructure> getCalculatedStructures() {
        return calculatedStructures;
    }
}
