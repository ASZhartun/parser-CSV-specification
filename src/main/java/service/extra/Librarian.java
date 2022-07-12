package service.extra;

import dao.ReaderCSVExtraUnit;
import entities.ExtraUnitStorage;
import entities.RebarCage;
import org.springframework.beans.factory.annotation.Autowired;

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
     *
     * @param path путь csv файла, содержащего таблицу КЖ.И
     */
    public void addNewRebarCageFrom(String path) {
        final ArrayList<String> read = readerCSVExtraUnit.read(path);
        RebarCage rebarCage = parserUnitCSV.parse(read);
        extraUnitStorage.addUnit(rebarCage);
    }

    /**
     * Возвращает объект RebarCage по наименованию.
     *
     * @param title наименование
     * @return объект RebarCage
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
