package logic.objects;

public class BusinessCaseRead {
    public int id;
    private String name;  //required
    private String code;
    //    public Person owner;
    public Company company; //required
    //    public Person person;
//    public String description;
//    public Dial category;
//    public Dial source;
//    public Date validTill;
//    public Date scheduledEnd;
//    public Dial businessCasePhase;
//    public List<Product> items;
    public transient Offer offer;

    public String asHTML() {
        return "<option value=\"" + id + "\">" + code + ":\t" + name + "</option>";
    }

}
