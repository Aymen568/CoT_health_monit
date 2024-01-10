package tn.cot.healthmonitoring.boundaries;

import jakarta.ejb.EJB;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.EmailDTO;
import tn.cot.healthmonitoring.services.EmailService;

@Path("/email")
public class EmailResource {
    @EJB
    private EmailService emailService;
    @POST
    @Path("/send")
    @Consumes("application/json")
    public Response sendEmail(EmailDTO emailDTO, @Context HttpServletRequest request) {
        try {
            emailService.sendEmail(emailDTO.getTo(), emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent());
            return Response.status(Response.Status.OK).entity("Email sent successfully").build();
        } catch (MessagingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to send email").build();
        }
    }
}