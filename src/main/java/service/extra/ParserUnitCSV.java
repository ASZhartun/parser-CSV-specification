package service.extra;

import entities.Line;
import entities.PositionBar;
import entities.RebarCage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUnitCSV {

    private ArrayList<String> content;

    public ParserUnitCSV() {

    }

    public RebarCage parse(ArrayList<String> content) {
        this.content = content;
        return parse();
    }

    public RebarCage parse() {
        final RebarCage rebarCage = new RebarCage();
        rebarCage.setTitle(content.get(0));
        for (int i = 1; i < content.size(); i++) {
            final String[] split = content.get(i).split(";");
            Arrays.stream(split).forEach((item) -> item = deleteQuotes(item));
            final Line line = new Line(split[0], " ", split[1], split[2]);
            if (line.getName().equals(" ")) {
                continue;
            }
            rebarCage.getBars().add(parseLine(line));
        }
        rebarCage.refresh();
        return rebarCage;
    }

    private PositionBar parseLine(Line line) {
        final PositionBar positionBar = new PositionBar();
        try {
            positionBar.setQuantity(Integer.parseInt(line.getQuantity()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            positionBar.setQuantity(0);
        }
        final Pattern compile = Pattern.compile("([0-9]+)[ ]+(S[0-9]{3})[ ]+.+L=([0-9]+)");
        final Matcher matcher = compile.matcher(line.getName());
        if (matcher.find()) {
            try {
                positionBar.setDiameter(Integer.parseInt(matcher.group(1)));

            } catch (NumberFormatException e) {
                e.printStackTrace();
                positionBar.setDiameter(0);
            }
            positionBar.setRebarType(matcher.group(2));

            try {
                positionBar.setLength(Integer.parseInt(matcher.group(3)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                positionBar.setLength(0);
            }
        }
        positionBar.calcWeight();
        return positionBar;
    }

    private String deleteQuotes(String item) {
        return item.replaceAll("\"", "");
    }



    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
}
