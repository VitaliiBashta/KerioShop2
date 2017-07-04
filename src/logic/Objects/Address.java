package logic.Objects;


public class Address {
    int id;
    String city;
    String country;
    String countyCode;
    String name;
    String province;
    String street;
    String zipCode;

    @Override
    public String toString() {
        return " Address{" + street + ", " + zipCode + ' ' + city + ' ' +
                 country +  '}';
    }
}
