package com.sirius.web.controller;
import com.sirius.web.model.User;
import com.sirius.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("logincontrol")
    public String control(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        User dbuser = userService.authenticate(username, password);
        if (dbuser != null){
            return "logincontrol";
        }
        else {
            return "redirect:/?errNo=1";
        }
    }

    @RequestMapping("logout")
    public String logout() {
        return "logout";
    }
}
