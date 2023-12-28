package tn.cot.healthmonitoring.exceptions;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UserNotAuthorizedExceptionMapper implements ExceptionMapper<UserNotAuthorizedException> {



    @Override
    public Response toResponse(UserNotAuthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}