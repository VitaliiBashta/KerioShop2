package logic.Objects;


public class ContactInfo {
    boolean primary;
    public String email;
    public String email2;
    public String tel1;
    public String tel2;
    public String www;


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (email!=null) result.append(email).append(" ");
        if (tel1!=null) result.append(tel1);
        result.insert(0,"{");
        result.append("}");
        return result.toString();
    }
}
