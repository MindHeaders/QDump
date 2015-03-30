package org.dataart.qdump.service.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Created by artemvlasov on 26/03/15.
 */
public class WebApplicationUtils {
    private static ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
    public static Response responseCreator(Status status, String jsonFieldName, String jsonValue) {
        return Response.status(status).entity(objectNode.put(jsonFieldName, jsonValue)).build();
    }
    public static Response responseCreator(Status status, String jsonFieldName, boolean jsonValue) {
        return Response.status(status).entity(objectNode.put(jsonFieldName, jsonValue)).build();
    }
    public static Response responseCreator(Status status, String jsonFieldName, long jsonValue) {
        return Response.status(status).entity(objectNode.put(jsonFieldName, jsonValue)).build();
    }
    public static Response responseCreator(Status status, Object entity) {
        return Response.status(status).entity(entity).build();
    }
    public static void exceptionCreator(Status status, String message) {
        throw new WebApplicationException(Response
                .status(status)
                .entity(objectNode.put("error", message))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
}
