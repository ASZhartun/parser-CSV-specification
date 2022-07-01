package config;

import dao.ReaderCSV.ReaderCSV;
import dao.WriterCSV.WriterCSV;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import service.*;

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
        return new WriterCSV();
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
}
