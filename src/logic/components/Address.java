package logic.components;


class Address {
    int id;
    String countyCode;
    String name;
    String province;
    private String city;
    private String country;
    private String street;
    private String zipCode;

    @Override
    public String toString() {
        return " Address{" + street + ", " + zipCode + ' ' + city + ' ' +
                 country +  '}';
    }
}
