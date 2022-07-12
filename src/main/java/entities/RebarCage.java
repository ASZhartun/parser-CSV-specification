package entities;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Универсальный объект хранения пространственного каркаса из арматурных стержней.
 */
public class RebarCage {
    /**
     * Список стержневых позиций
     */
    private ArrayList<PositionBar> bars = new ArrayList<>();
    /**
     * Табличное наименование
     */
    private String title;
    /**
     * Табличное обозначение
     */
    private String doc;
    /**
     * Количество единицы
     */
    private int quantity;
    /**
     * Вес единицы
     */
    private double unitWeight;

    public RebarCage() {

    }

    /**
     * Подсчет веса единицы
     * @param bars список стержневых позиций, входящих в каркас.
     * @return суммарный вес
     */
    private double calculateUnitWeight(ArrayList<PositionBar> bars) {
        return bars.stream().mapToDouble((item) -> item.getWeight() * item.getQuantity()).sum();
    }

    public ArrayList<PositionBar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<PositionBar> bars) {
        this.bars = bars;
        setUnitWeight(calculateUnitWeight(bars));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(double unitWeight) {
        this.unitWeight = unitWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RebarCage rebarCage = (RebarCage) o;
        return Objects.equals(title, rebarCage.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
