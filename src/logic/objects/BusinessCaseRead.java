package logic.objects;

import logic.Raynet;
import logic.dials.BusinessCaseStatus;
import logic.dials.Currency;
import logic.dials.Dial;
import logic.dials.SecurityLevel;

import java.util.Date;
import java.util.List;


public class BusinessCaseRead {
    public int id;
    public String name;  //required
    public String code;
    public Person owner;
    public Company company; //required
    public Person person;
    public double totalAmount;
    public double estimatedValue;
    public int probability;
    public String description;
    public Currency currency;  //required
    public double exchangeRate;
    public Dial category;
    public Dial source;
    public Date validTill;
    public Date scheduledEnd;
    public BusinessCaseStatus status;
    public Dial businessCasePhase;
    public List<Product> items;
    public transient Offer offer;
    SecurityLevel securityLevel;


    public BusinessCaseRead(String query, Raynet raynet, int id) {
        super();
        this.id = id;
    }

    public BusinessCaseRead(String query, Raynet raynet) {

    }

    public String asHTML() {
        return "<option value=\"" + id + "\">" + name + "</option>";
    }

}
