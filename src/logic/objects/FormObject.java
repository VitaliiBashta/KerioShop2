package logic.objects;

import logic.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class FormObject {
    //    final Double exchangeRate;
    final Integer source;
    public Integer businessCasePhase; //
    public String code;
    public final Integer owner;
    public Integer company; //required
    public String companyName;
    public Integer person;
    public Integer probability;
    public String description;
    public Integer currency;  //required
    public Integer category;
    public Date validFrom;
    public Date scheduledEnd;
    public String product;
    public Double price;
    public String discountPercent;
    public Integer count;
    public Number cost;
    public Number taxRate;
    //    public transient List<Product> items;
    String name; //required
    private transient BusinessCaseWrite businessCase;
    private transient OfferWrite offer;
    private transient ProductWrite productWrite;


    private FormObject() {
        this.name = "testForm";
        this.owner = 462; //Vitalii Bashta
        this.company = 2; //required   Zebra Systems.
//        this.person = 462; //Vitalii Bashta
        this.probability = 0;
        this.description = " ";
        this.currency = 15;  //required  CZK
//        this.exchangeRate = 0.0;
        this.category = 486;  //Kerio Connect
        this.source = 61;  // email
        this.validFrom = new Date(); // today
        this.scheduledEnd = new Date();  //today
        this.businessCasePhase = 42; // cenova nabidka
    }

    public FormObject(String query) {
        this();
        Map<String, String> params = Utils.urlParamsToMap(query);
        if (params.get("product") != null) this.name = params.get("product");
        if (params.get("person") != null) this.person = Integer.valueOf(params.get("person"));
        if (params.get("company") != null) this.company = Integer.valueOf(params.get("company"));
        if (params.get("companyName") != null) this.companyName = params.get("companyName");
        if (params.get("currency") != null) this.currency = Integer.valueOf(params.get("currency"));
        if (params.get("currency") != null) this.currency = Integer.valueOf(params.get("currency"));
        if (params.get("description") != null) this.description = params.get("description");
        if (params.get("businessCasePhase") != null)
            this.businessCasePhase = Integer.valueOf(params.get("businessCasePhase"));
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
        this.cost = this.price * (1 - Utils.DISTRIBUTOR_MARGIN);
        if (this.currency == 15) this.taxRate = 21;
        this.businessCase = new BusinessCaseWrite(this);
        this.productWrite = new ProductWrite(this);
        this.offer = new OfferWrite(this);
    }

    public ProductWrite getProduct() {
        return productWrite;
    }

    public BusinessCaseWrite getBusinessCase() {
        return businessCase;
    }

    public OfferWrite getOffer() {
        return offer;
    }
}

