package logic.objects;

import logic.components.PrimaryAddress;
import logic.dials.Dial;


public class Company {

    public int id;
    public String name;   //required
    public PrimaryAddress primaryAddress;
    Dial category;
    Dial contactSource;

    @Override
    public String toString() {
        String name = this.name.replace("&", "\\u0026");
        name = name.replace("\"", "\\\"");
        name = name.replace("\'", "\\u0027");
        if (id == 251) System.out.println("name:" + name);
        return "{\"data\":\"" + id + "\",\"value\":\"" + name + "\"}";
    }
}
