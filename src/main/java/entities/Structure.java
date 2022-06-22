package entities;

import java.util.ArrayList;

public class Structure {
    private String title;
    private ArrayList<PositionBar> positions;
    private Double concreteVolume;

    public Structure() {

    }

    public void addPosition(PositionBar positionBar) {
        this.positions.add(positionBar);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<PositionBar> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<PositionBar> positions) {
        this.positions = positions;
    }

    public Double getConcreteVolume() {
        return concreteVolume;
    }

    public void setConcreteVolume(Double concreteVolume) {
        this.concreteVolume = concreteVolume;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "title='" + title + '\'' +
                ", concreteVolume=" + concreteVolume +
                "m3}\n";
    }
}
