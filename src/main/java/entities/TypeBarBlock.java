package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Объект блока с арматурой определенного типа, который входит в состав ведомости элементов.
 */
public class TypeBarBlock {
    /**
     * Определенный тип арматуры для данного блока
     */
    private String barType;
    /**
     * Мапа с диаметрами, для которых просчитываются суммы позиций определенного типа арматуры, указанного в barType.
     */
    private HashMap<Integer, Double> diameterPositionWeights = new HashMap<>();
    /**
     * Суммарная масса блока
     */
    private Double totalBlockWeight = 0d;

    public TypeBarBlock() {

    }

    public String getBarType() {
        return barType;
    }

    public void setBarType(String barType) {
        this.barType = barType;
    }

    public HashMap<Integer, Double> getDiameterPositionWeights() {
        return diameterPositionWeights;
    }

    public void setDiameterPositionWeights(HashMap<Integer, Double> diameterPositionWeights) {
        this.diameterPositionWeights = diameterPositionWeights;
    }

    public Double getTotalBlockWeight() {
        return totalBlockWeight;
    }

    public void setTotalBlockWeight(Double totalBlockWeight) {
        this.totalBlockWeight = totalBlockWeight;
    }

    public void calculateTotalWeight() {
        diameterPositionWeights.values()
                .forEach((item) -> totalBlockWeight += item);
    }



    public final static TreeSet<Integer> diameters = new TreeSet<>(Stream.of(3, 4, 5, 6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 28, 30, 32, 36, 40).collect(Collectors.toList()));

}
