package entities;

import java.util.ArrayList;

/**
 * ?????? ?.?. ????????
 */
public class Structure {
    /**
     * ????????????
     */
    private String title;
    /**
     * ?????? ?????????? ???????
     */
    private ArrayList<PositionBar> positions;
    /**
     * ?????? ?????????? ????? ?? ????
     */
    private ArrayList<RebarMesh> rebarMeshes;
    /**
     * ?????? ?????????? ???????????????? ???????? ? ?????? ?.?. ????????.
     */
    private ArrayList<RebarCage> rebarCages;
    /**
     * ????? ??????
     */
    private Double concreteVolume;
    /**
     * ???????????? ??????
     */
    private String concreteDefinition;

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

    public ArrayList<RebarMesh> getRebarMeshes() {
        return rebarMeshes;
    }

    public void setRebarMeshes(ArrayList<RebarMesh> rebarMeshes) {
        this.rebarMeshes = rebarMeshes;
    }

    public String getConcreteDefinition() {
        return concreteDefinition;
    }

    public void setConcreteDefinition(String concreteDefinition) {
        this.concreteDefinition = concreteDefinition;
    }

    public ArrayList<RebarCage> getRebarCages() {
        return rebarCages;
    }

    public void setRebarCages(ArrayList<RebarCage> rebarCages) {
        this.rebarCages = rebarCages;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "title='" + title + '\'' +
                ", concreteVolume=" + concreteVolume +
                "m3}\n";
    }
}
