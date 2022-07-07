package entities;

public class RebarMesh {
    private PositionBar base;
    private PositionBar cross;
    private String doc = "√Œ—“ 23279-2012";
    private String rebarTypeByDoc;
    private Integer quantity;
    private String startNaming;

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

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getRebarTypeByDoc() {
        return rebarTypeByDoc;
    }

    public void setRebarTypeByDoc(String rebarTypeByDoc) {
        this.rebarTypeByDoc = rebarTypeByDoc;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStartNaming() {
        return startNaming;
    }

    public void setStartNaming(String startNaming) {
        this.startNaming = startNaming;
    }
}
