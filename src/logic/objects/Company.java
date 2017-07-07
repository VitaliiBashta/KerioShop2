package logic.objects;

import logic.components.PrimaryAddress;
import logic.dials.*;

import java.util.LinkedList;
import java.util.List;

public class Company {
    public int id;
    public String name;   //required
    public Person owner;
    public String notice;
    public String regNumber;
    public String taxNumber;
    public transient List<Person> employees = new LinkedList<>();
    boolean person;
    String lastName;
    String firtName;
    String titleBefore;
    String titleAfter;
    String salutation;
    Rating rating;
    State state;
    Role role;
    CompanyCategory category;
    ContactSource contactSource;
    EmployeesNumber employeesNumber;
    LegalForm legalForm;
    PaymentTerm paymentTerm;
    Turnover turnover;
    EconomyActivity economyActivity;
    TaxPayer taxPayer;
    String bankAccount;
    SecurityLevel securityLevel;
    private PrimaryAddress primaryAddress;

    public String asHTML(){

        return "<option value=\""+id+"\">"+name+"</option>";
    }
    @Override
    public String toString() {
        return name+ "{id=" + id + '}' + primaryAddress;
    }
}
