package logic.objects;


public class CompanySimple {
    private String data;
    private String value;

    public CompanySimple(int id, String name) {
        this.data = String.valueOf(id);
        this.value = name;
    }
}
