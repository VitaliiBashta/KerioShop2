package logic.objects;


import logic.components.ContactInfo;
import logic.components.PrimaryRelationship;

import java.util.List;

public class Person {
    public final int id;
    private String titleBefore;
    private String firstName;
    private String lastName;
    private String titleAfter;
    public PrimaryRelationship primaryRelationship;
    public List<String> tags;
    private ContactInfo contactInfo;

    public Person(int id) {
        this.id = id;
    }

    private String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{").append(id).append("}");
        if (titleBefore != null) result.append(titleBefore).append(" ");
        if (firstName != null) result.append(firstName).append(" ");
        if (lastName != null) result.append(lastName).append(" ");
        if (titleAfter != null) result.append(titleAfter).append(" ");

        return result.toString() + contactInfo;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return id == person.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String asHTML() {
        return "<option value=\"" + id + "\">" + fullName() + "</option>";
    }
}
