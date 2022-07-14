package service.base;

import dao.ReaderCSV;
import dao.WriterCSV;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ����������.
 */
public class Operator {
    private ReaderCSV readerCSV;
    private ParserCSV parserCSV;
    private Calculator calculator;
    private TypeSetter typeSetter;
    private WriterCSV writerCSV;

    public Operator() {

    }

    /**
     * ������ ���� � �������� �������������, ���������, ������ ��� ����� � ���������� � ����������� ������.
     *
     * @param path ����, ��� ����� �������� ����.
     */
    public void doWork(String path) {
        readerCSV.readCSV(path);
        parserCSV.parseTable(readerCSV.getContent());
        calculator.calculate(parserCSV.getStructures());
        typeSetter.build(calculator.getCalculatedStructureCreator(), calculator.getStructures());
        writerCSV.save(typeSetter.getBill(), "bill.csv", readerCSV.getPathFolder());
        writerCSV.save(typeSetter.getSpec(), "spec.csv", readerCSV.getPathFolder());
    }

    public ReaderCSV getReaderCSV() {
        return readerCSV;
    }

    @Autowired
    public void setReaderCSV(ReaderCSV readerCSV) {
        this.readerCSV = readerCSV;
    }

    public ParserCSV getParserCSV() {
        return parserCSV;
    }

    @Autowired
    public void setParserCSV(ParserCSV parserCSV) {
        this.parserCSV = parserCSV;
    }

    public Calculator getCalculator() {
        return calculator;
    }

    @Autowired
    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    public TypeSetter getTypeSetter() {
        return typeSetter;
    }

    @Autowired
    public void setTypeSetter(TypeSetter typeSetter) {
        this.typeSetter = typeSetter;
    }

    public WriterCSV getWriterCSV() {
        return writerCSV;
    }

    @Autowired
    public void setWriterCSV(WriterCSV writerCSV) {
        this.writerCSV = writerCSV;
    }
}
