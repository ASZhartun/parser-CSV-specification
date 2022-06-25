package service;

import entities.support.BarMesh;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RebarMeshParser {
    public static void main(String[] args) {
        String sample = "4— %%C5S500-100/%%C5S500-100 65x145 50+50/25";
        final Pattern compile = Pattern.compile(REGEX_SIGNATURE_WIRE_REBAR_MESH_BY_GROUPS);
        final Matcher matcher = compile.matcher(sample);
        while (matcher.find()) {
            System.out.println(matcher.group(1)); // первый диаметр, тип, шаг
            System.out.println(matcher.group(3)); // второй диаметр, тип, шаг
            System.out.println(matcher.group(5)); // длины стержнй
            System.out.println(matcher.group(6)); // доборные шаги
        }
    }

    private String content;

    private String outputs;

    private String twoLength;
    private ArrayList<String> signatures = new ArrayList<>();
    private ArrayList<BarMesh> BarMeshes = new ArrayList<>();


    public void build(String content) {
        this.content = content;
        if (content.charAt(0) == '4' || content.charAt(0) == '5') {
            final Pattern compile = Pattern.compile(REGEX_SIGNATURE_WIRE_REBAR_MESH_BY_GROUPS);
            final Matcher matcher = compile.matcher(content);
            if (matcher.find()) {
                this.signatures.add(matcher.group(1));  //первый диаметр, тип, шаг
                this.signatures.add(matcher.group(3)); // второй диаметр, тип, шаг
                this.twoLength = matcher.group(5);  // длины стержнй
                this.outputs = matcher.group(6);    // доборные шаги
            }
        } else {

        }

    }

    public void createBarMeshes() {
        for (String item :
                signatures) {
            BarMeshes.add(parseSignature(item));
        }
    }

    private BarMesh parseSignature(String item) {
        final Pattern compile = Pattern.compile(REGEX_FOR_PARSE_SIGNATURE);
        final Matcher matcher = compile.matcher(item);
        final BarMesh barMesh = new BarMesh();
        if(matcher.find()) {
//            barMesh.setDiameter(extractDiameter(matcher.group(1)));
            barMesh.setTypeBar(matcher.group(2));
            barMesh.setStep(matcher.group(3));
        }
        return barMesh;
    }


    public RebarMeshParser(String content) {
        this.content = content;
    }

    public RebarMeshParser() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

public final static String REGEX_FOR_PARSE_SIGNATURE = "";

    public final static String REGEX_SIGNATURE_WIRE_REBAR_MESH_BY_GROUPS = "";
    public final static String REGEX_SIGNATURE_REBAR_MESH_BY_GROUPS = "";

}
