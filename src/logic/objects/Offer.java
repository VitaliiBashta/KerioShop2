package logic.objects;


import logic.dials.BusinessCaseStatus;
import logic.dials.Currency;

import java.util.Date;

public class Offer {
    public int id;
    public String name;
    public String code;
    public Person owner;
    public Person person;
    public Company company;
    public BusinessCaseRead businessCase;
    public float totalAmount;
    public float estimatedValue;
    public int probability;
    public Date validFrom;
    public Date validTill;
    public Date scheduledEnd;
    public String description;
    public Currency currency;
    public BusinessCaseStatus status;



}
