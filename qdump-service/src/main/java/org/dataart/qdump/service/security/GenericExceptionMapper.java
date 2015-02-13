package org.dataart.qdump.service.security;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by artemvlasov on 09/02/15.
 */
@Provider
@Component
public class GenericExceptionMapper implements ExceptionMapper<ShiroException> {
    @Override
    public Response toResponse(ShiroException exception) {
        return Response.status(exception instanceof UnauthenticatedException ? Response.Status.UNAUTHORIZED : Response.Status.FORBIDDEN)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
    }
}
