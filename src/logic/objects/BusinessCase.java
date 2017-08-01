package logic.objects;

import logic.Methods;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.util.Date;

public class BusinessCase {
    private final String name;  //required
    private final long owner;
    private final long company; //required
    private final Integer person;
    private final int probability;
    private final long currency;  //required
    //    private final double exchangeRate;
    private final long category;
    private final long source = 42;
    private final Date validFrom;
    private final Date scheduledEnd;
    private final int businessCasePhase;
    private int id;
    public String code;
    private final String description;

    BusinessCase(FormObject formObject) {
        this.name = formObject.productFullNames.get(0);
        this.owner = formObject.owner;
        this.company = formObject.company;
        this.person = formObject.person;
        this.probability = formObject.probability;
        this.currency = formObject.currency;
//        this.exchangeRate = formObject.exchangeRate;
        this.category = formObject.category;
//        this.source = 42; //formObject.source;
        this.validFrom = formObject.validFrom;
        this.scheduledEnd = formObject.scheduledEnd;
        this.businessCasePhase = formObject.businessCasePhase;
        this.description = formObject.description;
    }

    public Integer createBusinessCaseInRaynet() {
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = Utils.objectToEntity(this);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        this.id = Utils.getCreatedId(Methods.sendPut(Utils.RAYNET_API_URL + "/businessCase/", entity));
        return this.id;
    }
}
