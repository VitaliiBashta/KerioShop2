package logic.objects;

import logic.Utils;

class Product {
    Integer id;
    private final String name;
    private final Double price;
    private final String partnerMargin;
    private final Number cost;
    private final Number taxRate;
    private final Integer count = 1;
    Integer offerId;

    Product(FormObject formObject, int i) {
        name = formObject.productFullNames.get(i);
        price = formObject.prices.get(i);
        partnerMargin = formObject.partnerMargin;
        cost = price * (1 - Utils.DISTRIBUTOR_MARGIN);
        taxRate = formObject.taxRate;
    }
}
