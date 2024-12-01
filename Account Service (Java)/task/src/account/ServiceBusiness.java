package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

@Service
public class ServiceBusiness {

    private final PayStubRepository payStubRepository;
    private final AppUserRepository appUserRepository;
    private final SecurityEventRepository securityRepository;
    private static final Map<Integer, String> monthMap = Map.ofEntries(
            entry(1, "January"),
            entry(2, "February"),
            entry(3, "March"),
            entry(4, "April"),
            entry(5, "May"),
            entry(6, "June"),
            entry(7, "July"),
            entry(8, "August"),
            entry(9, "September"),
            entry(10, "October"),
            entry(11, "November")
    );

    @Autowired
    public ServiceBusiness(PayStubRepository payStubRepository, AppUserRepository appUserRepository, SecurityEventRepository securityRepository) {
        this.payStubRepository = payStubRepository;
        this.appUserRepository = appUserRepository;
        this.securityRepository = securityRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addPayStubs(List<PayStubEntryDTO> requests) {
        for (PayStubEntryDTO request : requests) {
            PayStub entry = new PayStub();
            if (!appUserRepository.existsByEmail(request.getEmployee())) {
                throw new NotFoundException("User not found!");
            }
            entry.setEmployee(appUserRepository.findByEmail(request.getEmployee()).orElseThrow());
            entry.setPeriod(request.getPeriod());
            entry.setSalary(request.getSalary());

            if (payStubRepository.existsByEmployeeAndPeriod(entry.getEmployee(), entry.getPeriod())) {
                throw new PayPeriodException();
            }
            payStubRepository.save(entry);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePayStub(PayStubEntryDTO request) {
        PayStub entry;
        if (!appUserRepository.existsByEmail(request.getEmployee())) {
            throw new NotFoundException("User not found!");
        }
        AppUser employee = appUserRepository.findByEmail(request.getEmployee()).orElseThrow();
        entry = payStubRepository.findByEmployeeAndPeriod(employee, request.getPeriod()).orElseThrow();
        entry.setSalary(request.getSalary());
        payStubRepository.save(entry);
    }

    public List<PayStubDTO> getPayRecord(AppUser employee) {
        List<PayStub> records = payStubRepository.findByEmployeeId(employee.getId());
        records.sort((r1,r2) -> r2.getPeriod().compareTo(r1.getPeriod()));
        List<PayStubDTO> reports = new ArrayList<>();
        for (PayStub record : records ) {
            reports.add(createPayRecord(employee, record));
        }
        return reports;
    }

    public PayStubDTO getPayRecord(AppUser employee, String period) {
        if (!period.matches("(0[1-9]|1[0-2])-[0-9]{4}")) {
            throw new PeriodInvalidException();
        }
        PayStub record = payStubRepository.findByEmployeeAndPeriod(employee, period).orElseThrow();
        return createPayRecord(employee, record);
    }

    public List<SecurityEventDTO> getSecurityEvents() {
        List<SecurityEvent> securityEvents = (List<SecurityEvent>) securityRepository.findAll();
        return securityEvents.stream().map(SecurityEvent::createSecurityEventDTO).toList();
    }

    private PayStubDTO createPayRecord(AppUser employee, PayStub record) {
        PayStubDTO report = new PayStubDTO();
        report.setName(employee.getName());
        report.setLastname(employee.getLastname());
        report.setPeriod(getMonthNamePeriod(record));
        report.setSalary(toDollarsCents(record));
        return report;
    }

    private static String getMonthNamePeriod(PayStub record) {
        String[] periodComponents = record.getPeriod().split("-");
        return monthMap.get(Integer.parseInt(periodComponents[0])) + "-" + periodComponents[1];
    }

    private static String toDollarsCents(PayStub record) {
        long dollars = Math.floorDiv(record.getSalary(), 100);
        int cents = (int) (record.getSalary() - dollars * 100);
        return String.format("%d dollar(s) %d cent(s)", dollars, cents);
    }
}
