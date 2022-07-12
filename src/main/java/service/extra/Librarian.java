package service.extra;

import dao.ReaderCSVExtraUnit;
import entities.ExtraUnitStorage;
import entities.RebarCage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * ������-����������. ������������ ������ � ��������������� ����������������� ����������� ���������. ���������:
 * <ul>
 *     <li>������</li>
 *     <li>�������������� � RebarCage</li>
 *     <li>����� �� ����� ������� � �������������� ������� ���������</li>
 * </ul>
 */
public class Librarian {
    /**
     * ������ ��� ������ �� csv �����
     */
    private ReaderCSVExtraUnit readerCSVExtraUnit;
    /**
     * ������ ��� �������������� �������� csv ����� � ������ RebarCage.
     */
    private ParserUnitCSV parserUnitCSV;
    /**
     * ������, �������� ��������� ���������������� ��.�
     */
    private ExtraUnitStorage extraUnitStorage;

    /**
     * ��������� ����� ��.� � ���������.
     *
     * @param path ���� csv �����, ����������� ������� ��.�
     */
    public void addNewRebarCageFrom(String path) {
        final ArrayList<String> read = readerCSVExtraUnit.read(path);
        RebarCage rebarCage = parserUnitCSV.parse(read);
        extraUnitStorage.addUnit(rebarCage);
    }

    /**
     * ���������� ������ RebarCage �� ������������.
     *
     * @param title ������������
     * @return ������ RebarCage
     */
    public RebarCage getItemBy(String title) {
        return extraUnitStorage
                .getExtraUnits()
                .stream()
                .filter((item) -> item.getTitle().equals(title))
                .findFirst()
                .orElseGet(RebarCage::new);
    }

    public Librarian() {

    }

    public ReaderCSVExtraUnit getReaderCSVExtraUnit() {
        return readerCSVExtraUnit;
    }

    public void setReaderCSVExtraUnit(ReaderCSVExtraUnit readerCSVExtraUnit) {
        this.readerCSVExtraUnit = readerCSVExtraUnit;
    }

    public ParserUnitCSV getParserUnitCSV() {
        return parserUnitCSV;
    }

    @Autowired
    public void setParserUnitCSV(ParserUnitCSV parserUnitCSV) {
        this.parserUnitCSV = parserUnitCSV;
    }

    public ExtraUnitStorage getExtraUnitStorage() {
        return extraUnitStorage;
    }

    @Autowired
    public void setExtraUnitStorage(ExtraUnitStorage extraUnitStorage) {
        this.extraUnitStorage = extraUnitStorage;
    }
}
