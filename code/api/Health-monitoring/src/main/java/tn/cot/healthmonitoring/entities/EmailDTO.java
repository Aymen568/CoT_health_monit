package tn.cot.healthmonitoring.entities;

public class EmailDTO {
    private String from;
    private String to;
    private String subject;
    private String content;

    // Constructors, getters, and setters
    public EmailDTO() {
    }
    // Constructor for creating an instance with required fields
    public EmailDTO(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    // Getters and setters for each field

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}