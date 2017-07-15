package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import webAccess.Methods;


public class ProductWrite {
    private Integer id;
    private final String name;
    private final Double price;
    private final String discountPercent;
    private final Integer count;
    private final Number cost;
    private final Number taxRate;
    private final Integer businessCaseId;

    public ProductWrite(FormObject formObject) {
        this.name = formObject.product;
        this.price = formObject.price;
        this.discountPercent = formObject.discountPercent;
        this.count = formObject.count;
        this.cost = formObject.cost;
        this.taxRate = formObject.taxRate;
        this.businessCaseId = formObject.getBusinessCase().id;
    }

    private String getJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public Integer createProductInRaynet() {
        HttpEntity entity = new StringEntity(getJson(), ContentType.APPLICATION_JSON);
        String url = "/api/v2/businessCase/" + this.businessCaseId + "/item/";
        this.id = Utils.getCreatedId(Methods.sendPut(url, entity));
        return this.id;
    }

}