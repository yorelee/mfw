package com.skcc.modern.pattern.example.patternlabs01example.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.skcc.modern.pattern.example.patternlabs01example.exception.ResourceNotFoundException;
import ccom.skcc.modern.pattern.example.patternlabs01example.model.EmployeeEntity;
import com.skcc.modern.pattern.example.patternlabs01example.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/")
public class EmployeeController {

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService service;
    
    @Autowired
    CircuitBreakerFactory circuitBreakerFactory;


    @RequestMapping
    @ApiOperation(value = "getAllEmployees", notes = "전체 직원 조회", response= String.class)
    // @ApiResponse(code = 404, message = "Person not found")
    public String getAllEmployees(Model model) {
        logger.debug("call getAllEmployees");
        List<EmployeeEntity> list = service.getAllEmployees();
        model.addAttribute("employees", list);
        return "list-employees";
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id) throws ResourceNotFoundException {
        if (id.isPresent()) {
            EmployeeEntity entity = service.getEmployeeById(id.get());
            model.addAttribute("employee", entity);
        } else {
            model.addAttribute("employee", new EmployeeEntity());
        }
        return "add-edit-employee";
    }
  
    @RequestMapping(path = "/delete/{id}")
    public String deleteEmployeeById(Model model, @PathVariable("id") Long id) throws ResourceNotFoundException {
        service.deleteEmployeeById(id);
        return "redirect:/";
    }

    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdateEmployee(EmployeeEntity employee) {
        service.createOrUpdateEmployee(employee);
        return "redirect:/";
    }
    
    @RequestMapping(path = "/send/{id}")
    public String sendGiftEmployeeById(Model model, @PathVariable("id") Long id) throws ResourceNotFoundException {
        EmployeeEntity entity = service.getEmployeeById(id);
        List<EmployeeEntity> list = Arrays.asList(entity);
        model.addAttribute("employees", list);

        return circuitBreakerFactory.create("sendGitf").run(service.sendGiftSupplier(id), throwable -> {
            logger.error("service delay call failed ", throwable);
            return "send-circuit-employees";
        });
    }

    @RequestMapping(path = "/retrysend/{id}")
    public String retrySendGiftEmployeeById(Model model, @PathVariable("id") Long id) throws ResourceNotFoundException {
        EmployeeEntity entity = service.getEmployeeById(id);
        List<EmployeeEntity> list = Arrays.asList(entity);
        model.addAttribute("employees", list);

        return circuitBreakerFactory.create("retrySendGitf").run(service.retrySendGiftSupplier(id), throwable -> {
            logger.error("service delay call failed ", throwable);
            return "send-circuit-employees";
        });
    }
}
