package logic.jsonObjects;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class JsonOfferPdfExport {

    private String uuid;
    private String fileName;
    private String contentType;
    private String accessToken;
    private String instanceName;

    public String getRequest() throws UnsupportedEncodingException {

        return "https://app.raynet.cz/api/v2/exportBody/" +
                uuid + '/' +
                accessToken + '/' +
                instanceName + '/' +
                "?fileName=\"" + URLDecoder.decode(fileName, "UTF-8") +
                "\"&contentType=" + URLDecoder.decode(contentType, "UTF-8");
    }



}
