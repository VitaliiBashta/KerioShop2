package logic.Objects;


class Address {
    int id;
    private String city;
    private String country;
    String countyCode;
    String name;
    String province;
    private String street;
    private String zipCode;

    @Override
    public String toString() {
        return " Address{" + street + ", " + zipCode + ' ' + city + ' ' +
                 country +  '}';
    }
}
