package tn.cot.healthmonitoring.exceptions;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserForbiddenExceptionMapper implements ExceptionMapper<UserForbiddenException> {

    @Override

    public Response toResponse(UserForbiddenException exception) {

        return Response.status(Response.Status.FORBIDDEN).build();

    }

}