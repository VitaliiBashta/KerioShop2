package logic.components;


public class PrimaryAddress {
    int id;
    boolean primary;
    private Address address;
    private ContactInfo contactInfo;

    @Override
    public String toString() {
        return "" + address + contactInfo + " ";
    }
}
