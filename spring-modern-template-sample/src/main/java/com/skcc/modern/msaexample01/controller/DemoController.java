package <%=package_name%>.msaexample01.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class DemoController {
    
    private Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping
    public String home() {
        log.info("home called");
        return "I'll let it go at this";
    }
}
