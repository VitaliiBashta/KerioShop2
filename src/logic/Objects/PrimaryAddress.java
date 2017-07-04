package logic.Objects;

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
