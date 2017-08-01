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
    private int id;
    private String name;
    public String code;
    private final Integer owner;
    private final Integer person;
    private final Integer company;
    private final Date validFrom;
    private final Date expirationDate;
    private final String description;
    private final Integer offerStatus;


    private Integer businessCase;

    OfferWrite(FormObject formObject, int i) {
        name = formObject.productFullNames.get(i);
        owner = formObject.owner;
        person = formObject.person;
        company = formObject.company;
        validFrom = formObject.validFrom;
        expirationDate = formObject.scheduledEnd;
        description = formObject.description;
        offerStatus = 49; //Offered
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

    public Integer createOfferInRaynet(int businessCase) {
        this.businessCase = businessCase;
        String json = Utils.objectToEntity(this);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        id = Utils.getCreatedId(Methods.sendPut(Utils.RAYNET_API_URL + "/offer/", entity));
        return id;
    }

    public void sync() {
        Methods.sendPost(Utils.RAYNET_API_URL + "/offer/" + this.id + "/sync/", null);
    }

    public String asHTML() {
        return "<option value=\"" + id + "\">" + code + ":\t" + name + "</option>";
    }
}
