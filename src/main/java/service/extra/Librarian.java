package service.extra;

import dao.ReaderCSVExtraUnit;
import entities.ExtraUnitStorage;
import entities.RebarCage;

import java.util.ArrayList;

/**
 * Объект-контроллер. Осуществляет работу с дополнительными пользовательскими арматурными каркасами. Выполняет:
 * <ul>
 *     <li>Чтение</li>
 *     <li>Преобразование в RebarCage</li>
 *     <li>Поиск по имени каркаса и предоставление объекта Оператору</li>
 * </ul>
 */
public class Librarian {
    /**
     * Модуль для чтения из csv файла
     */
    private ReaderCSVExtraUnit readerCSVExtraUnit;
    /**
     * Модуль для преобразования контента csv файла в объект RebarCage.
     */
    private ParserUnitCSV parserUnitCSV;
    /**
     * Модуль, хранящий множество пользовательских КЖ.И
     */
    private ExtraUnitStorage extraUnitStorage;

    /**
     * Добавляет новую КЖ.И в хранилище.
     * @param path путь csv файла, содержащего таблицу КЖ.И
     */
    public void addNewRebarCageFrom(String path) {
        final ArrayList<String> read = readerCSVExtraUnit.read(path);
        RebarCage rebarCage = getParserUnitCSV().parse(read);
        extraUnitStorage.addUnit(rebarCage);
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

    public void setParserUnitCSV(ParserUnitCSV parserUnitCSV) {
        this.parserUnitCSV = parserUnitCSV;
    }
}
