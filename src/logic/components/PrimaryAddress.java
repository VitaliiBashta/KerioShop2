package logic.components;


public class PrimaryAddress {
    int id;
    boolean primary;
    public Address address;
    private ContactInfo contactInfo;

    @Override
    public String toString() {
        return "" + address + contactInfo + " ";
    }
}
