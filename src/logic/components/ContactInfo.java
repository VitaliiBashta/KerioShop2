package logic.components;


public class ContactInfo {
    private String email;
    private String tel1;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (email != null) result.append(email).append(" ");
        if (tel1 != null) result.append(tel1);
        result.insert(0, "{");
        result.append("}");
        return result.toString();
    }
}
