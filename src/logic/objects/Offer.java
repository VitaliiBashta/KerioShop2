package logic.objects;

import java.util.Date;

class Offer {
    int id;
    private final String name;
    private final Integer person;
    private final Integer company;
    private final Date validFrom;
    private String code;
    private final Date expirationDate;
    private final String description;
    private final Integer offerStatus;

    Integer businessCase;

    Offer(FormObject formObject, int i) {
        name = formObject.productFullNames.get(i);
        person = formObject.person;
        company = formObject.companyId;
        validFrom = formObject.validFrom;
        expirationDate = formObject.scheduledEnd;
        description = formObject.description;
        offerStatus = 49; //Offered
    }



}
