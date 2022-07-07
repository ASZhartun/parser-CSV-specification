package service;

import dao.ReaderCSV.ReaderCSV;
import entities.CalculatedStructure;
import entities.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Держатель двух сущностей по созданию таблиц: спецификация, ведомость.
 */
public class TypeSetter {
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
        bill = billPrinter.build(calculatedStructureCreator);
        spec = specPrinter.build(structures);
    }


    public SpecPrinter getSpecPrinter() {
        return specPrinter;
    }

    @Autowired
    public void setSpecPrinter(SpecPrinter specPrinter) {
        this.specPrinter = specPrinter;
    }

    public BillPrinter getBillPrinter() {
        return billPrinter;
    }

    @Autowired
    public void setBillPrinter(BillPrinter billPrinter) {
        this.billPrinter = billPrinter;
    }


    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
