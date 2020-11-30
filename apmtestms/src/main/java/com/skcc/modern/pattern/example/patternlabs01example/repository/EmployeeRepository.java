package com.skcc.modern.pattern.example.patternlabs01example.repository;

import com.skcc.modern.pattern.example.patternlabs01example.model.EmployeeEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository  extends CrudRepository<EmployeeEntity, Long> {
}
