package entities;

import java.util.ArrayList;

/**
 * Обсчитанная структура для включения ее в объект ведомости элементов.
 */
public class CalculatedStructure {
    /**
     * Наименование структуры
     */
    private String title;
    /**
     * Список с арматурными блоками, различаемые по типу арматуры, входящих в объект ведомости элементов.
     */
    private ArrayList<TypeBarBlock> typeBarBlocks = new ArrayList<>();
    /**
     * Суммарная масса всех элементов, входящих в структуру
     */
    private Double totalWeight = 0d;

    public CalculatedStructure() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TypeBarBlock> getTypeBarBlocks() {
        return typeBarBlocks;
    }

    public void setTypeBarBlocks(ArrayList<TypeBarBlock> typeBarBlocks) {
        this.typeBarBlocks = typeBarBlocks;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }
}
