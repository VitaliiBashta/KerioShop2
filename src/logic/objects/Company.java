package logic.objects;

import logic.components.PrimaryAddress;
import logic.dials.*;

import java.util.LinkedList;
import java.util.List;

public class Company {
    public int id;
    public String name;   //required
//    public Person owner;  //not needed
//    public String notice; //not needed
//    public String regNumber; //not needed
//    public String taxNumber; //not needed
    public final transient List<Person> employees = new LinkedList<>();
    public final transient List<BusinessCase> businessCases = new LinkedList<>();
    Rating rating;
    State state;
    Role role;
    Dial category;
    Dial contactSource;
    List<String> tags;
//    public PaymentTerm paymentTerm; //not needed
//    public Dial economyActivity;  // present
//    SecurityLevel securityLevel; //not needed
    public PrimaryAddress primaryAddress;

    @Override
    public String toString(){
        String name = this.name.replace("&","\\u0026");
        name = name.replace("\"","\\\"");
        name = name.replace("\'","\\u0027");
        if (id == 251) System.out.println("name:" + name);
        return "{\"data\":\"" + id + "\",\"value\":\"" + name +"\"}";
    }
}
