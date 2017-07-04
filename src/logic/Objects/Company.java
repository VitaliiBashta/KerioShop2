package logic.Objects;

import java.util.LinkedList;
import java.util.List;

public class Company {
    public int id;
    public String name;
    public String role;
    public String rating;
    public String regNumber;
    public String taxNumber;
    public PrimaryAddress primaryAddress;
    public Person owner;
    public String notice;
    public List<Person> employees ;

    public Company() {
        employees = new LinkedList<>();
    }

    @Override
    public String toString() {
        return name+ "{id=" + id + '}' + primaryAddress;
    }
}
