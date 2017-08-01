package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Methods;
import logic.Utils;
import logic.jsonObjects.JsonPerson;


public class PersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        String query = he.getRequestURI().getQuery();
        Integer companyId = Integer.valueOf(query);
        String response = getPersons(companyId);
        Utils.writeResponse(he, response);
    }

    private String getPersons(Integer companyId) {
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/person/?primaryRelationship-company-id=" + companyId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
        return jsonPerson.asHTML();
    }
}