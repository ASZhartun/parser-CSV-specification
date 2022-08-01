package service;

import org.springframework.beans.factory.annotation.Autowired;
import service.base.Operator;
import service.extra.Librarian;

public class Director {

    private Operator operator;
    private Librarian librarian;

    public Director() {

    }

    public void deleteRPC() {

    }

    @Autowired
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Autowired
    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public Operator getOperator() {
        return operator;
    }

    public Librarian getLibrarian() {
        return librarian;
    }
}
