package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Methods;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.util.Date;

public class OfferWrite {
    private final String name;
    private final Integer owner;
    private final Integer person;
    private final Integer company;
    private final Date validFrom;
    private final Date expirationDate;
    //    private final long category;
    private final String description;
    private final Integer offerStatus;
    public String code;
    private int id;
    private Integer businessCase;
    public OfferWrite(FormObject formObject) {
        this.name = formObject.name;
        this.owner = formObject.owner;
        this.person = formObject.person;
        this.company = formObject.company;
        this.businessCase = formObject.getBusinessCase().id;
        this.validFrom = formObject.validFrom;
        this.expirationDate = formObject.scheduledEnd;
        this.description = formObject.description;
//        this.category = formObject.category;
        this.offerStatus = 49; //Nabidnuta
    }

    public void setBusinessCase(Integer businessCase) {
        this.businessCase = businessCase;
    }

    private String getJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String result = gson.toJson(this);
        System.out.println(result);
        return result;
    }

    public Integer createOfferInRaynet() {
        HttpEntity entity = new StringEntity(getJson(), ContentType.APPLICATION_JSON);
        this.id = Utils.getCreatedId(Methods.sendPut(Utils.RAYNET_API_URL + "/offer/", entity));
        return this.id;
    }

    public void sync() {
        Methods.sendPost(Utils.RAYNET_API_URL + "/offer/" + this.id + "/sync/", null);
    }

}
