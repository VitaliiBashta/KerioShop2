package logic.objects;

import logic.Raynet;
import logic.dials.BusinessCaseStatus;
import logic.dials.Currency;
import logic.dials.Dial;
import logic.dials.SecurityLevel;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class BusinessCaseWrite {
    public int id;
    public String name;  //required
    public String code;
    public long owner;
    public long company; //required
    public long person;
    public double totalAmount;
    public double estimatedValue;
    public int probability;
    public String description;
    public long currency;  //required
    public double exchangeRate;
    public long category;
    public long source;
    public Date validFrom;
    public Date scheduledEnd;
    public BusinessCaseStatus status;
    public int businessCasePhase;
    public List<Product> items;
    public transient Offer offer;
    SecurityLevel securityLevel;

    public BusinessCaseWrite() {
    }

    public BusinessCaseWrite(Map<String,String> params, Raynet raynet) {
        if (params.get("name") !=null) this.name =params.get("name");

    }
}
