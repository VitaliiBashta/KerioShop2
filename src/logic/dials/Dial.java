package logic.dials;


public class Dial {
    public int id;
    public String code01;

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"code01\":\"" + code01 + '\"' + '}';
    }

    public String asHtmlOption() {
        return "<option value=\"" + id + "\">" + code01 + "</option>";
    }
}
