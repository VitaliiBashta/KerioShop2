package logic.jsonObjects;

import java.util.List;


public class JsonCompany {
    public int totalCount;
    public List<Company> data;

    public class Company {
        public int id;
        public String name;   //required
    }
}
