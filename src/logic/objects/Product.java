package logic.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Methods;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class Product {
    private Integer id;
    private final String name;
    private final Double price;
    private final String discountPercent;
    private final Number cost;
    private final Number taxRate;
    private final Integer count = 1;
    private Integer offerId;

    Product(FormObject formObject, int i) {
        name = formObject.productFullNames.get(i);
        price = formObject.prices.get(i);
        discountPercent = formObject.discountPercent;
        cost = price * (1 - Utils.DISTRIBUTOR_MARGIN);
        taxRate = formObject.taxRate;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    private String getJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public Integer createProductInRaynet(Integer offerId) {
        this.offerId = offerId;
        String json = Utils.objectToEntity(this);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        String url = Utils.RAYNET_API_URL + "/offer/" + this.offerId + "/item/";
        this.id = Utils.getCreatedId(Methods.sendPut(url, entity));
        return this.id;
    }

}
