package tn.cot.healthmonitoring.boundaries;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Properties;

@Path("/send-email")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {

    @POST
    public Response sendEmail(EmailData emailData) {
        // Implement the logic to send the email using JavaMail or any other email library

        // Example JavaMail logic (you need to configure your email server details)
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "your-smtp-server.com");
            // Set other properties as needed

            Session session = Session.getInstance(props, null);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your-email@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailData.getTo()));
            message.setSubject(emailData.getSubject());
            message.setText(emailData.getBody());

            Transport.send(message);

            return Response.ok("Email sent successfully").build();
        } catch (MessagingException e) {
            return Response.status(500).entity("Error sending email").build();
        }
    }
}