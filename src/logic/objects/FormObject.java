package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Raynet;
import logic.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormObject {
    String name; //required
    public String code;
    public Integer owner;
    public Integer company; //required
    public Integer person;
    public Double totalAmount;
    public Double estimatedValue;
    public Integer probability;
    public String description;
    public Integer currency;  //required
    public Double exchangeRate;
    public Integer category;
    public Integer source;
    public Date validFrom;
    public Date scheduledEnd;
    public Integer businessCasePhase; //

    public String product;
    public Double price;
    public Number discountPercent;
    public Integer count;
    public Number cost;
    public Number taxRate;
    public transient List<Product> items;
    private transient BusinessCaseWrite businessCase;
    private transient OfferWrite offer;



    public String getJsonOffer(Integer businessCaseId) {
        this.offer = new OfferWrite(this);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(offer);
    }

    public BusinessCaseWrite getBusinessCase() {
        if (businessCase == null)
            this.businessCase = new BusinessCaseWrite(this);
        return businessCase;
    }

    public OfferWrite getOffer() {
        if (offer == null)
            this.offer = new OfferWrite(this);
        return offer;
    }

    private FormObject() {
        this.name = "testForm";
        this.owner = 462; //Vitalii Bashta
        this.company = 2; //required   Zebra Systems.
        this.person = 462; //Vitalii Bashta
        this.totalAmount = 0.0;
        this.estimatedValue = 0.0;
        this.probability = 0;
        this.description = " ";
        this.currency = 15;  //required  CZK
        this.exchangeRate = 0.0;
        this.category = 486;  //Kerio Connect
        this.source = 61;  // email
        this.validFrom = new Date(); // today
        this.scheduledEnd = new Date();  //today
        this.businessCasePhase = 42; // cenova nabidka
    }

    public FormObject(String query) {
        this();
        Map<String, String> params = Utils.split(query);
        if (params.get("name") != null) this.name = params.get("name");
        if (params.get("person") != null) this.person = Integer.valueOf((params.get("person")));
        if (params.get("company") != null) this.company = Integer.valueOf((params.get("company")));
        if (params.get("currency") != null) this.currency = Integer.valueOf((params.get("currency")));
        if (params.get("owner") != null) this.owner = Integer.valueOf((params.get("owner")));
        if (params.get("validFrom") != null) {
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            try {
                this.validFrom = format.parse(params.get("validFrom"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (params.get("scheduledEnd") != null) {
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            try {
                this.scheduledEnd = format.parse(params.get("scheduledEnd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (params.get("probability") != null) this.probability = Integer.parseInt(params.get("probability"));

    }
}
