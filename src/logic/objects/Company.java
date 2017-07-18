package logic.objects;

import logic.components.PrimaryAddress;
import logic.dials.Dial;
import logic.dials.Rating;
import logic.dials.Role;
import logic.dials.State;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Company {
    //    public Person owner;  //not needed
//    public String notice; //not needed
//    public String regNumber; //not needed
//    public String taxNumber; //not needed
    public final transient Set<Person> employees = new HashSet<>();
    public final transient Set<BusinessCaseRead> businessCases = new HashSet<>();
    public int id;
    public String name;   //required
    //    public PaymentTerm paymentTerm; //not needed
//    public Dial economyActivity;  // present
//    SecurityLevel securityLevel; //not needed
    public PrimaryAddress primaryAddress;
    Rating rating;
    State state;
    Role role;
    Dial category;
    Dial contactSource;
    List<String> tags;

    @Override
    public String toString() {
        String name = this.name.replace("&", "\\u0026");
        name = name.replace("\"", "\\\"");
        name = name.replace("\'", "\\u0027");
        if (id == 251) System.out.println("name:" + name);
        return "{\"data\":\"" + id + "\",\"value\":\"" + name + "\"}";
    }
}
