package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import webAccess.Methods;

import java.util.Date;
import java.util.List;

public class BusinessCaseWrite {
    public int id;
    private final String name;  //required
    public String code;
    private final long owner;
    private final long company; //required
    private final long person;
    private final int probability;
    public String description;
    private final long currency;  //required
    private final double exchangeRate;
    private final long category;
    private final long source;
    private final Date validFrom;
    private final Date scheduledEnd;
    private final int businessCasePhase;
    public List<Product> items;
    public transient Offer offer;

    public BusinessCaseWrite(FormObject formObject) {
        this.name = formObject.name;
        this.owner = formObject.owner;
        this.company = formObject.company;
        this.person = formObject.person;
        this.probability = formObject.probability;
        this.currency = formObject.currency;
        this.exchangeRate = formObject.exchangeRate;
        this.category = formObject.category;
        this.source = formObject.source;
        this.validFrom = formObject.validFrom;
        this.scheduledEnd = formObject.scheduledEnd;
        this.businessCasePhase = formObject.businessCasePhase;
    }

    public Integer createBusinessCaseInRaynet() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(this);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        this.id = Utils.getCreatedId(Methods.sendPut("/api/v2/businessCase/", entity));
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessCaseWrite that = (BusinessCaseWrite) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
