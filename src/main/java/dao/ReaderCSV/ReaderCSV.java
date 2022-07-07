package dao.ReaderCSV;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>DONE.</h1>
 * <h2>Description</h2>
 * <p>Read table.csv from resources. Keep path of folder, path of csv file, content of csv file.</p>
 */
public class ReaderCSV {
    private String pathCSV;
    private String pathFolder;
    private final ArrayList<String> content = new ArrayList<>();

    public ReaderCSV() {

    }

    public void readCSV(String path) {
        this.pathCSV = path;
        setPathFolder(path);
        try {
            final FileReader fileReader = new FileReader(pathCSV);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                content.add(temp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
        } catch (IOException e) {
            System.out.println("Line is not found");
        }
    }

    public String getPathFolder(String path) {
        return this.pathFolder;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public String getPathCSV() {
        return pathCSV;
    }

    public void setPathCSV(String pathCSV) {
        this.pathCSV = pathCSV;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String path) {
        final Pattern compile = Pattern.compile("(.+\\\\)");
        final Matcher matcher = compile.matcher(path);
        String group = "C:\\";
        if (matcher.find()) {
            group = matcher.group(1);
        }
        this.pathFolder = group;
    }
}
