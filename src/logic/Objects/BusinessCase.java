package logic.Objects;

import java.util.Date;
import java.util.List;


public class BusinessCase {
    private final int id;
    public String code;
    private String name;  //required
    private int currency;  //required
    private Company company; //required
    private Person person;
    private Person owner;
    public Date validFrom;
    public Date validTill;
    public Date scheduledEnd;
    public String description;
    public int status;
    private int businessCasePhase;
    public List<Product> items;

    public BusinessCase(int id, String name, int currency, Company company, Person person, Person owner, int businessCasePhase) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.company = company;
        this.person = person;
        this.owner = owner;
        this.businessCasePhase = businessCasePhase;
    }
}
