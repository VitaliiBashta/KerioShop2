package logic.Objects;


public class PrimaryRelationship {
    int id;
    public Company company;

    @Override
    public String toString() {
        return  ""+company;
    }
}
