package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import webAccess.Methods;

import java.util.Date;

public class OfferWrite {
    public int id;
    public String name;
    public String code;
    public Integer owner;
    public Integer person;
    public Integer company;
    public Integer businessCase;
    public Date validFrom;
    public Date expirationDate;
    public String description;
    public Integer offerStatus;

    public OfferWrite(FormObject formObject) {
        this.name = formObject.name;
        this.owner = formObject.owner;
        this.person = formObject.person;
        this.company = formObject.company;
        this.businessCase = formObject.getBusinessCase().id;
        this.validFrom = formObject.validFrom;
        this.expirationDate = formObject.scheduledEnd;
        this.description = formObject.description;
        this.offerStatus = 49; //Nabidnuta
    }

    private String getJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public Integer createOfferInRaynet(){
        HttpEntity entity = new StringEntity(getJson(), ContentType.APPLICATION_JSON);
        this.id = Utils.getCreatedId(Methods.sendPut("/api/v2/offer/", entity));
        return this.id;
    }

    public boolean sync() {
        String result = Methods.sendPost("/api/v2/offer/" + this.id + "/sync/", null);
        return result.equals("{\"success\":\"true\"}");
    }
}
