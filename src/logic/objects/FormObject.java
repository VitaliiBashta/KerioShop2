package logic.objects;

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
    public Integer probability;
    public final String description;
    public Integer currency;  //required
    public final Double exchangeRate;
    public Integer category;
    public final Integer source;
    public Date validFrom;
    public Date scheduledEnd;
    public final Integer businessCasePhase; //
    public String product;
    public Double price;
    public String discountPercent;
    public Integer count;
    public Number cost;
    public Number taxRate;
    public transient List<Product> items;
    private transient BusinessCaseWrite businessCase;
    private transient OfferWrite offer;
    private transient ProductWrite productWrite;


    public ProductWrite getProduct() {
        if (productWrite == null)
            this.productWrite = new ProductWrite(this);
        return productWrite;
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
        if (params.get("product") != null) this.name = params.get("product");
        if (params.get("person") != null) this.person = Integer.valueOf((params.get("person")));
        if (params.get("company") != null) this.company = Integer.valueOf((params.get("company")));
        if (params.get("currency") != null) this.currency = Integer.valueOf((params.get("currency")));
        if (params.get("owner") != null) this.owner = Integer.valueOf((params.get("owner")));
        try {
            if (params.get("validFrom") != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                this.validFrom = format.parse(params.get("validFrom"));
            }
            if (params.get("scheduledEnd") != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                this.scheduledEnd = format.parse(params.get("scheduledEnd"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (params.get("probability") != null) this.probability = Integer.parseInt(params.get("probability"));
        if (params.get("category") != null) this.category = Integer.parseInt(params.get("category"));
        if (params.get("product") != null) this.product = params.get("product");
        if (params.get("price") != null) this.price = Double.valueOf(params.get("price"));
        if (params.get("discountPercent") != null) this.discountPercent = params.get("discountPercent");
        this.count = 1;
        this.cost = this.price * (1 - Raynet.DISTRIBUTOR_MARGIN);
        if (this.currency == 15) this.taxRate = 21;
    }
}

