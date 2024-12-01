package account;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

@Validated
@JsonPropertyOrder({"employee", "period", "salary"})
public class PayStubEntryDTO {
    private String employee;

    @Pattern(regexp = "(0[1-9]|1[0-2])-[0-9]{4}")
    private String period;

    @Positive
    private Long salary;

    public PayStubEntryDTO() {}

    public PayStubEntryDTO(String employee, String period, Long salary) {
        //employee email
        this.employee = employee;
        //mm-YYYY
        this.period = period;
        //in cents
        this.salary = salary;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
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
