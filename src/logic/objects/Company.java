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
    Dial category;
    Dial contactSource;
    Dial employeesNumber;
    Dial legalForm;
    PaymentTerm paymentTerm;
    Dial turnover;
    Dial economyActivity;
    TaxPayer taxPayer;
    String bankAccount;
    SecurityLevel securityLevel;
    private PrimaryAddress primaryAddress;

    public String asHTML(){
        return "<option value=\""+id+"\">"+name+"</option>";
    }
    @Override
    public String toString(){
        String name = this.name.replace("&","\\u0026");
        name = name.replace("\"","\\\"");
        name = name.replace("\'","\\u0027");
        if (id == 251) System.out.println("name:" + name);
        return "{\"data\":\"" + id + "\",\"value\":\"" + name +"\"}";
    }
}
