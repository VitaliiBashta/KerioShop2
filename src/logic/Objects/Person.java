package logic.Objects;


public class Person {
    public final int id;
    public String titleBefore;
    public String firstName;
    public String lastName;
    public String titleAfter;
    public Person owner;
    public PrimaryRelationship primaryRelationship;
    public ContactInfo contactInfo;

    public Person(int id) {
        this.id = id;
    }

    public String fullName() {
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

    public String asHTML() {
        return "<option value=\"" + id + "\">" + fullName() + "</option>";
    }
}
