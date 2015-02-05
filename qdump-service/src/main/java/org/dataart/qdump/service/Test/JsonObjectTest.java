package org.dataart.qdump.service.Test;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by artemvlasov on 04/02/15.
 */
public class JsonObjectTest {
    public static void main(String[] args) throws JSONException {
        boolean isValid = true;
        JSONObject object = new JSONObject();
        object.put("isValid", isValid);
        System.out.println(object);
    }
}
