package dao.WriterCSV;

import java.io.*;
import java.util.ArrayList;

public class WriterCSV {
    private String pathFolder;

    public WriterCSV(String path) {
        this.pathFolder = path;
    }

    public WriterCSV() {
    }

    public void writeCSV(ArrayList<String> strings) {
        final String content = convertArrayListToString(strings);
        try {
            final PrintWriter printWriter = new PrintWriter(pathFolder + "result.csv");
            printWriter.write(content);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCSV(String content) {
        try {
            final PrintWriter printWriter = new PrintWriter(pathFolder + "result.csv");
            printWriter.write(content);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertArrayListToString(ArrayList<String> content) {
        final StringBuilder sb = new StringBuilder();
        content.stream().forEach((item) -> {
            sb.append(item);
            sb.append("\n");
        });
        return sb.toString();
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }
}
