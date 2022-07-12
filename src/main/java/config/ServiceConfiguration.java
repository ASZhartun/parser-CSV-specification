package config;

import dao.ReaderCSV;
import dao.WriterCSV;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import service.base.*;
import service.extra.Librarian;
import service.extra.ParserUnitCSV;

@Configuration
public class ServiceConfiguration {
    @Bean("reader")
    @Scope("singleton")
    public ReaderCSV getReaderCSV() {
        return new ReaderCSV();
    }

    @Bean("writer")
    @Scope("singleton")
    public WriterCSV getWriterCSV() {
        return new WriterCSV("C:\\");
    }

    @Bean("operator")
    @Scope("singleton")
    public Operator getOperator() {
        return new Operator();
    }

    @Bean("parserCSVSpecificationTable")
    @Scope("singleton")
    public ParserCSV getParserCSV() {
        return new ParserCSV();
    }

    @Bean("calculator")
    @Scope("singleton")
    public Calculator getCalculator() {
        return new Calculator();
    }

    @Bean("rebarMeshParser")
    @Scope("singleton")
    public RebarMeshParser getRebarMeshParser() {
        return new RebarMeshParser();
    }

    @Bean("calculatedStructureCreator")
    @Scope("singleton")
    public CalculatedStructureCreator getCalculatedStructureCreator() {
        return new CalculatedStructureCreator();
    }

    @Bean("billPrinter")
    @Scope("singleton")
    public BillPrinter getBillPrinter() {
        return new BillPrinter();
    }

    @Bean("specPrinter")
    @Scope("singleton")
    public SpecPrinter getSpecPrinter() {
        return new SpecPrinter();
    }

    @Bean("typeSetter")
    @Scope("singleton")
    public TypeSetter getTypeSetter() {
        return new TypeSetter();
    }

    @Bean("librarian")
    @Scope("singleton")
    public Librarian getLibrarian() {
        return new Librarian();
    }

    @Bean("parserUnitCSV")
    @Scope("singleton")
    public ParserUnitCSV getParserUnitCSV() {
        return new ParserUnitCSV();
    }
}
