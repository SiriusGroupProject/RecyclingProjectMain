package com.sirius.web.controller;

import com.sirius.web.model.Bottle;
import com.sirius.web.service.BottleService;
import com.sirius.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class MappingController {
    // inject via application.properties
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @Autowired
    private BottleService bottleService;

    @RequestMapping("/map")
    public String dashboard(Map<String, Object> model) {
        return "map";
    }

}
