package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Raynet;
import logic.Utils;
import logic.dials.BusinessCaseStatus;
import logic.dials.Currency;
import logic.dials.Dial;
import logic.dials.SecurityLevel;
import logic.jsonObjects.JsonResponse;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import webAccess.Methods;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BusinessCaseWrite {
    public int id;
    public String name;  //required
    public String code;
    public long owner;
    public long company; //required
    public long person;
    public double totalAmount;
    public double estimatedValue;
    public int probability;
    public String description;
    public long currency;  //required
    public double exchangeRate;
    public long category;
    public long source;
    public Date validFrom;
    public Date scheduledEnd;
    public BusinessCaseStatus status;
    public int businessCasePhase;
    public List<Product> items;
    public transient Offer offer;
    SecurityLevel securityLevel;

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

    public String getJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public Integer createBusinessCaseInRaynet(){
        HttpEntity entity = new StringEntity(getJson(), ContentType.APPLICATION_JSON);
        this.id = Utils.getCreatedId(Methods.sendPut("/api/v2/businessCase/", entity));
        return this.id;
    }



}
