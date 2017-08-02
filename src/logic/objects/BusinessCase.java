package logic.objects;

import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.util.Date;

import static logic.SendMethod.PUT;
import static logic.Utils.sendRequest;

public class BusinessCase {
    private final String name;  //required
    private final long owner;
    private final long company; //required
    private final Integer person;
    private final int probability;
    private final long currency;  //required
    private final long category;
    private final long source = 61; //email
    private final Date validFrom;
    private final Date scheduledEnd;
    private final int businessCasePhase;
    private int id;
    public String code;
    private final String description;

    BusinessCase(FormObject formObject) {
        this.name = formObject.productFullNames.get(0);
        this.owner = formObject.owner;
        this.company = formObject.companyId;
        this.person = formObject.person;
        this.probability = formObject.probability;
        this.currency = formObject.currency;
        this.category = formObject.category;
        this.validFrom = formObject.validFrom;
        this.scheduledEnd = formObject.scheduledEnd;
        this.businessCasePhase = formObject.businessCasePhase;
        this.description = formObject.description;
    }

    public Integer createBusinessCaseInRaynet() {
        String json = Utils.objectToJson(this);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        this.id = Utils.getCreatedId(sendRequest(Utils.RAYNET_URL + "/businessCase/", PUT, entity));
        return this.id;
    }
}
