package logic.jsonObjects;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class JsonBusinessCase {
    private final List<BusinessCase> data = new LinkedList<>();

    private class BusinessCase {
        public int id;
        private String name;  //required
        private String code;
    }

    public String asHTML() {
        StringBuilder result = new StringBuilder();
        result.append("<option selected disabled>(total: ").append(this.data.size()).append(" OP)</option>");
        result.append("<option value=\"0\">(new)</option>");
        Collections.reverse(data);
        for (BusinessCase aData : data) {
            result.append("<option value=\"").append(aData.id).append("\">")
                    .append(aData.code).append(":\t")
                    .append(aData.name).append("</option>");
        }
        return result.toString();
    }
}
