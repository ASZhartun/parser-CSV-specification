import config.EntityConfiguration;
import config.ServiceConfiguration;
import dao.ReaderCSV.ReaderCSV;
import dao.WriterCSV.WriterCSV;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.Operator;
import service.ParserCSV;


import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EntityConfiguration.class);
        context.register(ServiceConfiguration.class);
        context.refresh();
        ReaderCSV readerCSV = (ReaderCSV) context.getBean("reader");
        readerCSV.setPathCSV("D:\\portfolio\\parser-CSV-specification\\src\\main\\resources\\table.txt");
        readerCSV.setPathFolder("D:\\portfolio\\parser-CSV-specification\\src\\main\\resources\\table.txt");
        readerCSV.readCSV();
        ParserCSV parserCSV = (ParserCSV) context.getBean("parserCSVSpecificationTable");
        parserCSV.parseTable(readerCSV.getContent());



    }
}
