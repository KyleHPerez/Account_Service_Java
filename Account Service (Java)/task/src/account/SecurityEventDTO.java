package account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "date", "action", "subject", "object", "path"})
public class SecurityEventDTO {
    private long id;
    private LocalDateTime date;
    private String action;
    private String subject; // user email
    private String object;
    private String path;

    public SecurityEventDTO() {}

    public SecurityEventDTO(long id, LocalDateTime date, String action, String subject, String object, String path) {
        this.id = id;
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
