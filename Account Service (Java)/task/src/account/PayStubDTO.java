package account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "lastname", "period", "salary"})
public class PayStubDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("period")
    private String period;
    @JsonProperty("salary")
    private String salary;

    public PayStubDTO() {}

    public PayStubDTO(String name, String lastname, String period, String salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = period;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
