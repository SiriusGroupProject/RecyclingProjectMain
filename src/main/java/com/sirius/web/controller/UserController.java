package com.sirius.web.controller;

import com.sirius.web.model.User;
import com.sirius.web.service.UserService;
import com.sirius.web.utils.HashingForPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserController implements Serializable {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("listUsers")
    public ResponseEntity<List<User>> findAll() {
        final List<User> users = userService.getAllUsers();

        if (users == null) {
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PostMapping("addUser")
    public ResponseEntity<User> createUser(@RequestBody User user) throws NoSuchAlgorithmException {

        if (user.getEmail() != null && user.getEmail().length() != 0 && !userService.exists(user.getEmail())) {
            String encpassword = HashingForPassword.hash(user.getPassword());
            user.setPassword(encpassword);

            final User dbuser = userService.createUser(user);
            return new ResponseEntity<User>(dbuser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        try {
            final User dbuser = userService.findUserByEmail(email);
            if (dbuser == null) {
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateUser")
    public ResponseEntity<User> updateUser(@RequestBody User user) throws NoSuchAlgorithmException {

        final boolean isExist = userService.exists(user.getEmail());
        if (isExist) {
            String hashedPassword = HashingForPassword.hash(user.getPassword());
            user.setPassword(hashedPassword);
            userService.updateUser(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity deleteUser(@RequestParam String email){
        try {
            boolean ok = userService.deleteUser(email);
            return new ResponseEntity(ok, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity(false, HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        boolean ok = userService.deleteAll();
        return new ResponseEntity(ok, HttpStatus.NO_CONTENT);
    }

    @GetMapping("login")
    public boolean loginUser(@RequestParam String email, @RequestParam String password) {
        boolean dbUserIsExist = userService.authenticate(email, password);
       if(!dbUserIsExist) {
           return false;
       }
       return true;
    }

    @PutMapping("updateBalance/{email}/{balance}")
    public boolean updateBalance(@PathVariable("email") String email, @PathVariable("balance")String balance){
        User dbUser = userService.findUserByEmail(email);
        if(dbUser == null) {
            return false;
        }
        dbUser.setBalance(dbUser.getBalance()+ Float.parseFloat(balance));
        userService.updateUser(dbUser);
        return true;
    }
}
