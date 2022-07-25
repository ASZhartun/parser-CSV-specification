package dao;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Читает файл с csv таблицей пользовательской КЖ.И
 */
public class ReaderCSVExtraUnit {
    private String path;
    private String filename;

    public ReaderCSVExtraUnit() {

    }

    public ArrayList<String> read(String path) {
        this.path = path;
        return read();
    }

    /**
     * Возвращает массив строк таблицы КЖ.И. Прим., первый элемент - имя файла.
     * @return ArrayList<String>
     */
    public ArrayList<String> read() {
        final File file = new File(path);
        filename = file.getName();
        final FileReader fileReader;
        try {
            fileReader = new FileReader(file);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            final ArrayList<String> content = new ArrayList<>();
            content.add(filename.split("\\.")[0]);
            content.addAll(bufferedReader.lines().collect(Collectors.toList()));
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
