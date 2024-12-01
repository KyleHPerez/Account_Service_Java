package account;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime date;
    private String action;

    @ManyToOne
    @JoinColumn(nullable = true)
    private AppUser user = new AppUser();
    private String subject; // user email
    private String object;
    private String path;

    public SecurityEvent() {
        this.subject = null == user.getEmail() ? "Anonymous" : user.getEmail();
    }

    public SecurityEvent(long id, LocalDateTime date, String action, AppUser user, String object, String path) {
        this.id = id;
        this.date = date;
        this.action = action;
        this.user = user;
        this.subject = null == user.getEmail() ? "Anonymous" : user.getEmail();
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

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
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

    public SecurityEventDTO createSecurityEventDTO() {
        SecurityEventDTO securityEventDTO = new SecurityEventDTO();
        securityEventDTO.setId(this.id);
        securityEventDTO.setDate(this.date);
        securityEventDTO.setAction(this.action);
        securityEventDTO.setSubject(this.subject);
        securityEventDTO.setObject(this.object);
        securityEventDTO.setPath(this.path);
        return securityEventDTO;
    }
}
