package com.skcc.modern.pattern.example.patternlabs01example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.skcc.modern.pattern.example.patternlabs01example.exception.ResourceNotFoundException;
import com.skcc.modern.pattern.example.patternlabs01example.model.EmployeeEntity;
import com.skcc.modern.pattern.example.patternlabs01example.repository.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class EmployeeService {

    Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private MeterRegistry meterRegistry;
    private Counter callAllEmployees;
    private Counter callEmployeeById;
    private Counter callCreateOrUpdateEmployee;
    private Counter callDeleteEmployee;

    public EmployeeService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        initMetrics();
    }

    private void initMetrics() {
        this.callAllEmployees = Counter.builder("custom_employee_call_allemployees_total")
                        .tags("tagkey", "tagvalue")   
                        .description("Total getAllEmployees service count")
                        .register(meterRegistry);
        this.callEmployeeById = Counter.builder("custom_employee_call_employees_id_total")
                        .description("Total getEmployeeById service count")
                        .register(meterRegistry);
        this.callCreateOrUpdateEmployee = Counter.builder("custom_employee_call_createorupdate_total")
                        .description("Total createOrUpdateEmployee service count")
                        .register(meterRegistry);
        this.callDeleteEmployee = Counter.builder("custom_employee_call_deletebyid_total")
                        .description("Total deleteByEmployee service count")
                        .register(meterRegistry);
        // TODO : callSendGiftEmployyById 정의
        // sendGiftEmployeeById 에 대한 호출 counter을 metric으로 추가

        // TODO : callRetrySendGiftEmployeeById
        // retrySendGiftEmployeeById 에 대한 호출 counter을 metric으로 추가
    }

    @Autowired
    EmployeeRepository repository;

    public List<EmployeeEntity> getAllEmployees() {
        logger.debug("call getAllEmployees");
        callAllEmployees.increment();
        List<EmployeeEntity> result = (List<EmployeeEntity>) repository.findAll();

        if (result.size() > 0) {
        return result;
        } else {
        return new ArrayList<EmployeeEntity>();
        }
    }

    public EmployeeEntity getEmployeeById(Long id) throws ResourceNotFoundException {
        callEmployeeById.increment();
        Optional<EmployeeEntity> employee = repository.findById(id);

        if (employee.isPresent()) {
        return employee.get();
        } else {
        throw new ResourceNotFoundException("No employee record exist for given id");
        }
    }

    public EmployeeEntity createOrUpdateEmployee(EmployeeEntity entity) {
        callCreateOrUpdateEmployee.increment();
        if (entity.getId() == null) {
            entity = repository.save(entity);

            return entity;
        } else {
            Optional<EmployeeEntity> employee = repository.findById(entity.getId());

            if (employee.isPresent()) {
                EmployeeEntity newEntity = employee.get();
                newEntity.setEmail(entity.getEmail());
                newEntity.setFirstName(entity.getFirstName());
                newEntity.setLastName(entity.getLastName());
                newEntity = repository.save(newEntity);

                return newEntity;
            } else {
                entity = repository.save(entity);
                return entity;
            }
        }
    }

    public void deleteEmployeeById(Long id) throws ResourceNotFoundException {
        callDeleteEmployee.increment();
        Optional<EmployeeEntity> employee = repository.findById(id);

        if (employee.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No employee record exist for given id");
        }
    }

    public String sendGiftEmployeeById(Long id) {
        try {
            int sleep = id.intValue() * 1000;
            logger.debug("Sleep time(s) : " + sleep);
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "send-employees";
    }
    
    public String retrySendGiftEmployeeById(Long id) {
        try {
            int sleep = (int)(Math.random() * 10 * 1000);
            logger.debug("Sleep time(s) : " + sleep);
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            return "send-employees";
        }

        public Supplier<String> sendGiftSupplier(final Long id) {
        return () -> this.sendGiftEmployeeById(id);
    }

    public Supplier<String> retrySendGiftSupplier(final Long id) {
        return () -> this.retrySendGiftEmployeeById(id);
    }
    
}
