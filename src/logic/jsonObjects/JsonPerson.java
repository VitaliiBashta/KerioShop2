package logic.jsonObjects;

import logic.components.ContactInfo;

import java.util.List;

public class JsonPerson {
    private List<Person> data;

    public String asHTML() {
        StringBuilder result = new StringBuilder();
        for (Person aData : data) {
            result.append("<option value=\"").append(aData.id).append("\">")
                    .append(aData.fullName()).append("</option>");
        }
        return result.toString();
    }

    private class Person {
        private int id;
        private String firstName;
        private String lastName;
        private ContactInfo contactInfo;

        private String fullName() {
            return firstName + " " + lastName;
        }
    }
}
