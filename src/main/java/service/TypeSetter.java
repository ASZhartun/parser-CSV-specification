package service;

import dao.ReaderCSV.ReaderCSV;
import entities.CalculatedStructure;
import entities.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class TypeSetter {
    private ReaderCSV readerCSV;
    private SpecPrinter specPrinter;
    private BillPrinter billPrinter;

    private String bill;
    private String spec;

    public TypeSetter() {

    }

    /**
     * Метод формирует таблицы-строки из объектов структур
     *
     * @param calculatedStructureCreator объект, хранящий объекты предварительно подготовленных обсчитанных структур
     * @param structures                 список распаршенных структур
     */
    public void build(CalculatedStructureCreator calculatedStructureCreator, ArrayList<Structure> structures) {
//        bill = billPrinter.build(calculatedStructureCreator);
        spec = specPrinter.build(structures);
    }





    public SpecPrinter getSpecPrinter() {
        return specPrinter;
    }

    public void setSpecPrinter(SpecPrinter specPrinter) {
        this.specPrinter = specPrinter;
    }

    public BillPrinter getBillPrinter() {
        return billPrinter;
    }

    public void setBillPrinter(BillPrinter billPrinter) {
        this.billPrinter = billPrinter;
    }

    public ReaderCSV getReaderCSV() {
        return readerCSV;
    }

    @Autowired
    public void setReaderCSV(ReaderCSV readerCSV) {
        this.readerCSV = readerCSV;
    }
}
