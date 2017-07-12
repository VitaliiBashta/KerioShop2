package logic.components;

import logic.dials.Territory;

public class PrimaryAddress {
    int id;
    boolean primary;
    public Address address;
    private ContactInfo contactInfo;
    public Territory territory;

    @Override
    public String toString() {
        return "" + address + contactInfo + " ";
    }
}
