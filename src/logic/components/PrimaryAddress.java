package logic.components;

import logic.dials.Territory;

public class PrimaryAddress {
    int id;
    boolean primary;
    Address address;
    ContactInfo contactInfo;
    Territory territory;

    @Override
    public String toString() {
        return "" + address + contactInfo + " ";
    }
}
