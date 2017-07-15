package logic.objects;

import logic.Raynet;
import logic.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final Double totalAmount;
    private final Double estimatedValue;
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
        if (params.get("product") != null) this.name = params.get("product");
        if (params.get("person") != null) this.person = Integer.valueOf((params.get("person")));
        if (params.get("company") != null) this.company = Integer.valueOf((params.get("company")));
        if (params.get("currency") != null) this.currency = Integer.valueOf((params.get("currency")));
        if (params.get("owner") != null) this.owner = Integer.valueOf((params.get("owner")));
        if (params.get("validFrom") != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                this.validFrom = format.parse(params.get("validFrom"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (params.get("scheduledEnd") != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                this.scheduledEnd = format.parse(params.get("scheduledEnd"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

    public static void main(String[] args) throws ParseException {

        String testDateString = "2017-07-14";
        String testDateString2 = "02-04-2014 23:37:50";
        String testDateString3 = "02-Apr-2014";
        String testDateString4 = "04 02, 2014";
        String testDateString5 = "Thu, Apr 02 2014";
        String testDateString6 = "Thu, Apr 02 2014 23:37:50";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateFormat df3 = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat df4 = new SimpleDateFormat("MM dd, yyyy");
        DateFormat df5 = new SimpleDateFormat("E, MMM dd yyyy");
        DateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        try {
            //format() method Formats a Date into a date/time string.
            Date d1 = df.parse(testDateString);
            System.out.println("Date: " + d1);
            System.out.println("Date in dd/MM/yyyy format is: " + df.format(d1));

            Date d2 = df2.parse(testDateString2);
            System.out.println("Date: " + d2);
            System.out.println("Date in dd-MM-yyyy HH:mm:ss format is: " + df2.format(d2));

            Date d3 = df3.parse(testDateString3);
            System.out.println("Date: " + d3);
            System.out.println("Date in dd-MMM-yyyy format is: " + df3.format(d3));

            Date d4 = df4.parse(testDateString4);
            System.out.println("Date: " + d4);
            System.out.println("Date in MM dd, yyyy format is: " + df4.format(d4));

            Date d5 = df5.parse(testDateString5);
            System.out.println("Date: " + d5);
            System.out.println("Date in E, MMM dd yyyy format is: " + df5.format(d5));

            Date d6 = df6.parse(testDateString6);
            System.out.println("Date: " + d6);
            System.out.println("Date in E, E, MMM dd yyyy HH:mm:ss format is: " + df6.format(d6));

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

