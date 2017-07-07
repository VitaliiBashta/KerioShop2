package logic.components;


import logic.objects.Company;

public class PrimaryRelationship {
    public Company company;
    int id;

    @Override
    public String toString() {
        return  ""+company;
    }
}
