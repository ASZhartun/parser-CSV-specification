package entities;

public class Line {
    private String pos;
    private String description;
    private String name;
    private String quantity;
    private String weight;
    private String note;

    public Line(String pos, String description, String name, String quantity, String weight, String note) {
        this.pos = pos;
        this.description = description;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
        this.note = note;
    }

    public Line() {

    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return getPos() + '\t'
                + getDescription() + '\t'
                + getName() + '\t'
                + getQuantity() + '\t'
                + getWeight() + '\t'
                + getNote() + '\n';
    }
}
