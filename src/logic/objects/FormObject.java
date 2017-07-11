package logic.objects;

import logic.dials.BusinessCaseStatus;

import java.util.Date;
import java.util.List;

public class FormObject {
    String name;
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
    public int businessCasePhase;
    public List<Product> items;
    public transient Offer offer;
}
