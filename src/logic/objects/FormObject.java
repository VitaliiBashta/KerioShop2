package logic.objects;

import java.util.*;

public class FormObject {
    //    final Double exchangeRate;
    Integer source = 42;
    public Integer businessCasePhase; //
    public String code;
    public Integer owner;
    public Integer company; //required
    public String companyName;
    public Integer person;
    public Integer probability;
    public String description;
    public Integer currency;  //required
    public Integer category;
    public Date validFrom;
    public Date scheduledEnd;

    public String discountPercent;
    private Integer count;
    public Number taxRate;

    public boolean offersSeparate;
    private String productFullName;
    private String price;
    private String product1;
    private String price1;
    private String product2;
    private String price2;
    private String product3;
    private String price3;
    public final transient List<String> productFullNames = new ArrayList<>();
    public final transient List<Double> prices = new ArrayList<>();

    String name; //required
    private transient BusinessCase businessCase;
    private final transient List<OfferWrite> offers = new ArrayList<>();
    public final transient List<Product> products = new ArrayList<>();


//    private FormObject() {
//        this.name = "testForm";
//        this.owner = 462; //Vitalii Bashta
//        this.company = 2; //required   Zebra Systems.
////        this.person = 462; //Vitalii Bashta
//        this.probability = 0;
//        this.description = " ";
//        this.currency = 15;  //required  CZK
////        this.exchangeRate = 0.0;
//        this.category = 486;  //Kerio Connect
//        this.source = 61;  // email
//        this.validFrom = new Date(); // today
//        this.scheduledEnd = new Date();  //today
//        this.businessCasePhase = 42; // cenova nabidka
//    }


    public BusinessCase getBusinessCase() {
        if (businessCase == null)
            businessCase = new BusinessCase(this);
        return businessCase;
    }

    public Product getProduct(int i) {
        return products.get(i);
    }

    public void init() {
        this.productFullNames.clear();
        this.productFullNames.add(this.productFullName);
        this.prices.add(Double.valueOf(this.price));
        if (!this.product1.equals("")) {
            productFullNames.add(product1);
            prices.add(Double.valueOf(price1));
        }
        if (!product2.equals("")) {
            productFullNames.add(product2);
            prices.add(Double.valueOf(price2));
        }
        if (!product3.equals("")) {
            productFullNames.add(product3);
            prices.add(Double.valueOf(price3));
        }

        offers.clear();
        products.clear();

        for (int i = 0; i < productFullNames.size(); i++) {
            offers.add(new OfferWrite(this, i));
        }

        for (int i = 0; i < this.productFullNames.size(); i++) {
            products.add(new Product(this, i));
        }

        this.count = 1;

        if (this.currency == 15) this.taxRate = 21;
    }

    public OfferWrite getOffer(int i) {
        return offers.get(i);
    }
}

