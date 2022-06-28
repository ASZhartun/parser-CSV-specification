package entities;

import java.util.HashMap;

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
}
