package logic.objects;

import logic.Utils;

class Product {
    Integer id;
    private final String name;
    private final Double price;
    private final String discountPercent;
    private final Number cost;
    private final Number taxRate;
    private final Integer count = 1;
    Integer offerId;

    Product(FormObject formObject, int i) {
        name = formObject.productFullNames.get(i);
        price = formObject.prices.get(i);
        discountPercent = formObject.discountPercent;
        cost = price * (1 - Utils.DISTRIBUTOR_MARGIN);
        taxRate = formObject.taxRate;
    }

//    public Integer createProductInRaynet(Integer offerId) {
//        this.offerId = offerId;
//        String json = Utils.objectToJson(this);
//        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
//        String url = Utils.RAYNET_URL + "/offer/" + this.offerId + "/item/";
//        this.id = Utils.getCreatedId(sendRequest(url, PUT, entity));
//        return this.id;
//    }

}
