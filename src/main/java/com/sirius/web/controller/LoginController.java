package com.sirius.web.controller;
import com.sirius.web.model.User;
import com.sirius.web.service.UserService;
import com.sirius.web.utils.HashingForPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("logincontrol")
    public String control(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        boolean dbUserIsExist = userService.authenticate(email, password);
        if (dbUserIsExist){
            User user = userService.findUserByEmail(email);
            if (user.isAdmin())
                return "logincontrol";
            else
                return "redirect:/?errNo=2";
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
