package entities;

import java.util.HashMap;

public class StructureBill {
    private String title;

    private HashMap<String, Double> bars;

    private Double total = 0.0;

    public StructureBill() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Double> getBars() {
        return bars;
    }

    public void setBars(HashMap<String, Double> bars) {
        this.bars = bars;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void calculateTotal() {
        bars.values().forEach((item) -> total += item);
    }
}
