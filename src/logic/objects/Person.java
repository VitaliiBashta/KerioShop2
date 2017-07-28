package logic.objects;

import logic.components.ContactInfo;

public class Person {
    public final int id;
    private String firstName;
    private String lastName;
    private ContactInfo contactInfo;

    public Person(int id) {
        this.id = id;
    }

    private String fullName() {
        return firstName + " " + lastName;
    }

    public String asHTML() {
        return "<option value=\"" + id + "\">" + fullName() + "</option>";
    }
}
