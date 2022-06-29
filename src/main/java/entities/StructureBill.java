package entities;

import java.util.ArrayList;
import java.util.HashMap;

public class StructureBill {
    private String header;
    private ArrayList<CalculatedStructure> calculatedStructures = new ArrayList<>();

    public StructureBill() {

    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<CalculatedStructure> getCalculatedStructures() {
        return calculatedStructures;
    }

    public void setCalculatedStructures(ArrayList<CalculatedStructure> calculatedStructures) {
        this.calculatedStructures = calculatedStructures;
    }
}
