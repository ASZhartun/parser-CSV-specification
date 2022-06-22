package service;

import dao.ReaderCSV.ReaderCSV;
import dao.WriterCSV.WriterCSV;
import org.springframework.beans.factory.annotation.Autowired;

public class Operator {
    private ReaderCSV readerCSV;
    private ParserCSV parserCSV;

    public void read(String path) {
        readerCSV.setPathCSV(path);
        readerCSV.setPathFolder(path);
        readerCSV.readCSV();
    }

    public void parseCSV() {
        parserCSV.parseTable(readerCSV.getContent());
    }

    public Operator() {

    }

    public Operator(ReaderCSV readerCSV, ParserCSV parserCSV) {
        this.readerCSV = readerCSV;
        this.parserCSV = parserCSV;
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
}
