package logic.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormObject {
    Integer source = 42;

    public final transient List<Offer> offers = new ArrayList<>();
    Integer person;
    final transient List<String> productFullNames = new ArrayList<>();
    Integer currency;  //required
    final transient List<Double> prices = new ArrayList<>();
    public boolean offersSeparate;
    public Integer companyId; //required
    public transient BusinessCase businessCase;
    String discountPercent;
    Integer owner;
    Date validFrom;
    Date scheduledEnd;
    //    public String code;

    private String productFullName;
    private String price;
    private String product1;
    private String price1;
    private String product2;
    private String price2;
    private String product3;
    private String price3;
    Integer businessCasePhase;
    private Integer count = 1;

    public final transient List<Product> products = new ArrayList<>();
    Integer category;
    Integer probability;
    String description;
    Number taxRate = 0;

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

        if (this.currency == 15) this.taxRate = 21;

        offers.clear();
        products.clear();

        for (int i = 0; i < productFullNames.size(); i++) {
            offers.add(new Offer(this, i));
        }

        for (int i = 0; i < this.productFullNames.size(); i++) {
            products.add(new Product(this, i));
        }
        businessCase = new BusinessCase(this);
    }
}