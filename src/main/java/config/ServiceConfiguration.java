package config;

import dao.ReaderCSV.ReaderCSV;
import dao.WriterCSV.WriterCSV;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import service.Operator;
import service.ParserCSV;

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
}
