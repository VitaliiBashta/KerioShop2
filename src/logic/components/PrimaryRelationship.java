package logic.components;


import logic.objects.Company;

public class PrimaryRelationship {
    private Company company;
    int id;

    @Override
    public String toString() {
        return "" + company;
    }
}
