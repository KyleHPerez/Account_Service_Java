package account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
@Entity
public class PayStub {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private AppUser employee;

    @NotBlank
    @Pattern(regexp = "(0[1-9]|1[0-2])-[0-9]{4}")
    private String period;
    private Long salary;

    public PayStub() {}

    public PayStub(Long id, AppUser employee, String period, Long salary) {
        this.id = id;
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getEmployee() {
        return employee;
    }

    public void setEmployee(AppUser employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
