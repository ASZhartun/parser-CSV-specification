package entities;

import java.util.ArrayList;

public class BlockTable {
    private ArrayList<Line> lines = new ArrayList<>();
    private Integer index;

    public BlockTable(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public BlockTable() {

    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
