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
public class InsertBottleController {
    // inject via application.properties
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @Autowired
    private BottleService bottleService;

    @RequestMapping("/insertbottle")
    public String dashboard(Map<String, Object> model) {
        return "insertbottle";
    }

    @RequestMapping("insertbottlecontrol")
    public String control(@RequestParam String barcode, @RequestParam String brand,@RequestParam String type,
                          @RequestParam float price, @RequestParam float volume, HttpServletRequest request) {
        Bottle bottle = new Bottle();
        bottle.setBarcode(barcode);
        bottle.setName(brand);
        bottle.setType(type);
        bottle.setPrice(price);
        bottle.setVolume(volume);
        bottleService.createBottle(bottle);
        return "redirect:/insertbottle";
    }
}
