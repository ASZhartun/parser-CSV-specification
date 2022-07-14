package dao;

import java.io.*;

public class WriterCSV {

    private String pathForSaving;

    public WriterCSV(String pathForSaving) {
        this.pathForSaving = pathForSaving;
    }

    public WriterCSV() {

    }

    public void save(String table, String filename, String pathForSaving) {
        final File file = new File(pathForSaving, filename);
        PrintWriter out;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.write(table);
            out.close();
        } catch (IOException e) {
            System.out.println("Не записало " + filename + "!");
            e.printStackTrace();
        }

    }

    public String getPathForSaving() {
        return pathForSaving;
    }

    public void setPathForSaving(String pathForSaving) {
        this.pathForSaving = pathForSaving;
    }
}
