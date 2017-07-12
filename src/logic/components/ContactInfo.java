package logic.components;


public class ContactInfo {
    public String email2;  //some has
    public String tel2;   //some has
    public String www; //some has
    boolean primary;
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
