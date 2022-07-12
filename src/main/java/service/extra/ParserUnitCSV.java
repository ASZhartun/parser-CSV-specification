package service.extra;

import entities.RebarCage;

import java.util.ArrayList;

public class ParserUnitCSV {
    private ArrayList<String> content;

    public ParserUnitCSV() {

    }

    public RebarCage parse(ArrayList<String> content) {
        this.content = content;
        return parse();
    }

    public RebarCage parse() {
        return null;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
}
