package logic.objects;


import logic.dials.Currency;

import java.util.Date;

public class Offer {
    public int id;
    public String name;
    public String code;
    public Person owner;
    public Person person;
    public Company company;
    public BusinessCaseRead businessCase;
    public float totalAmount;
    public float estimatedValue;
    public int probability;
    public Date validFrom;
    public Date validTill;
    public Date scheduledEnd;
    public String description;
    public Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        return id == offer.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
