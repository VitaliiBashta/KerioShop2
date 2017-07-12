package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Utils;
import logic.jsonObjects.JsonOfferPdfExport;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import webAccess.Methods;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class OfferWrite {
    private int id;
    private final String name;
    public String code;
    public final Integer owner;
    private final Integer person;
    public final Integer company;
    private final Integer businessCase;
    private final Date validFrom;
    private final Date expirationDate;
    private final String description;
    public final Integer offerStatus;

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

    public void sync() {
        String result = Methods.sendPost("/api/v2/offer/" + this.id + "/sync/", null);
    }

    public String getPdfUrl() throws UnsupportedEncodingException {
        String request = "/api/v2/offer/" + this.id + "/pdfExport";
        String response = Methods.sendGet(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonOfferPdfExport json = gson.fromJson(response, JsonOfferPdfExport.class);

        return json.getRequest();
    }

}
