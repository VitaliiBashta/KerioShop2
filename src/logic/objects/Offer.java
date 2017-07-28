package logic.objects;

public class Offer {
    public int id;
    public String name;
    public String code;
    //    public Person owner;
//    public Person person;
//    public Company company;
    public BusinessCaseRead businessCase;
//    public float totalAmount;
//    public float estimatedValue;
//    public int probability;
//    public Date validFrom;
//    public Date validTill;
//    public Date scheduledEnd;
//    public String description;


    public String asHTML() {
        return "<option value=\"" + id + "\">" + code + ":\t" + name + "</option>";
    }
}
