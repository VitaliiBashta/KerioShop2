package logic.jsonObjects;

import java.util.List;

public class JsonOffer {
    public boolean success;
    public int totalCount;
    private List<OfferRead> data;

    private class OfferRead {
        private int id;
        private String name;
        private String code;
    }

    public String asHTML() {
        StringBuilder result = new StringBuilder();
        for (OfferRead aData : this.data) {
            result.append("<option value=\"")
                    .append(aData.id).append("\">")
                    .append(aData.code).append(":\t")
                    .append(aData.name)
                    .append("</option>");
        }
        return result.toString();
    }
}
