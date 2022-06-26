package entities;

public class RebarMesh {
    private PositionBar base;
    private PositionBar cross;
    private String doc = "√Œ—“ 23279-2012";

    public RebarMesh(PositionBar base, PositionBar cross) {
        this.base = base;
        this.cross = cross;
    }

    public RebarMesh() {

    }

    public PositionBar getBase() {
        return base;
    }

    public void setBase(PositionBar base) {
        this.base = base;
    }

    public PositionBar getCross() {
        return cross;
    }

    public void setCross(PositionBar cross) {
        this.cross = cross;
    }

    public String getDoc() {
        return doc;
    }
}
