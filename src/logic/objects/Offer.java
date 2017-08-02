package logic.objects;

import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.util.Date;

import static logic.SendMethod.POST;
import static logic.SendMethod.PUT;
import static logic.Utils.sendRequest;

public class Offer {
    private int id;
    private final String name;
    //    private final Integer owner;
    private final Integer person;
    private final Integer company;
    private final Date validFrom;
    private String code;
    private final Date expirationDate;
    private final String description;
    private final Integer offerStatus;


    private Integer businessCase;

    Offer(FormObject formObject, int i) {
        name = formObject.productFullNames.get(i);
//        owner = formObject.owner;
        person = formObject.person;
        company = formObject.companyId;
        validFrom = formObject.validFrom;
        expirationDate = formObject.scheduledEnd;
        description = formObject.description;
        offerStatus = 49; //Offered
    }

    public Integer createOfferInRaynet(int businessCase) {
        this.businessCase = businessCase;
        String json = Utils.objectToJson(this);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        id = Utils.getCreatedId(sendRequest(Utils.RAYNET_URL + "/offer/", PUT, entity));
        return id;
    }

    public void sync() {
        sendRequest(Utils.RAYNET_URL + "/offer/" + this.id + "/sync/", POST, null);
    }

}
