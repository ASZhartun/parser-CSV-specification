package service;

import dao.ReaderCSV.ReaderCSV;
import entities.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Класс, который формирует csv финальную спецификацию элементов.
 */
public class SpecPrinter {

    private ReaderCSV readerCSV;

    public SpecPrinter() {
    }

    private String makeSpecHeader() {
        return readerCSV.getContent().get(0) +
                readerCSV.getContent().get(1);
    }

    public ReaderCSV getReaderCSV() {
        return readerCSV;
    }

    @Autowired
    public void setReaderCSV(ReaderCSV readerCSV) {
        this.readerCSV = readerCSV;
    }

    /**
     * Формирует финальную спецификацию из списка структур
     * @param structures список структур
     * @return строковое представление таблицы
     */
    public String build(ArrayList<Structure> structures) {
        StringBuilder spec = new StringBuilder();
        spec.append(makeSpecHeader());
        for (Structure structure :
                structures) {
            spec.append(getBodyTableFrom(structure));
        }
        return spec.toString();
    }

    private String getBodyTableFrom(Structure structure) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(createStructureTitle(structure));

        return stringBuilder.toString();
    }

    private String createStructureTitle(Structure structure) {
        return ";" +
                ";" +
                structure.getTitle() +
                ";" +
                ";\n";
    }

    private Boolean checkRebarMeshesExisting(Structure structure) {
        return false;
    }

    private Boolean checkPositionBarsExisting(Structure structure) {
        return false;
    }
}
