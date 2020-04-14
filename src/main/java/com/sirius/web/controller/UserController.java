package com.sirius.web.controller;

import com.sirius.web.model.User;
import com.sirius.web.service.UserService;
import com.sirius.web.utils.HashingForPassword;
import com.sirius.web.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("listUsers")
    public ResponseEntity<List<User>> findAllUsers() {
        final List<User> users = userService.getAllUsers();

        if (users == null) {
            logger.error("&" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Registered users could not be listed");
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }
        logger.info("&" + Util.trace(Thread.currentThread().getStackTrace()) + " ***List of registered users : " + users);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PostMapping("addUser")
    public ResponseEntity<User> createUser(@RequestBody User user) throws NoSuchAlgorithmException {

        if (user.getEmail() != null && user.getEmail().length() != 0 && !userService.exists(user.getEmail())) {
            String encpassword = HashingForPassword.hash(user.getPassword());
            user.setPassword(encpassword);

            final User dbuser = userService.createUser(user);
            logger.info("#" + dbuser.getEmail() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***New user has been created. Information of the new user : " + dbuser);
            return new ResponseEntity<User>(dbuser, HttpStatus.CREATED);
        } else {
            logger.error("#" + user.getEmail() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Failed to create new user");
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {

        final User dbuser = userService.findUserByEmail(email);
        if (dbuser != null) {
            logger.info("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***User information : " + dbuser);
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        } else {
            logger.error("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***User information not found");
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
            logger.info("#" + user.getEmail() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***User information has been updated");
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            logger.error("#" + user.getEmail() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity deleteUser(@RequestParam String email) {

        if(userService.exists(email)) {
            userService.deleteUser(email);
            logger.info("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***The user has been deleted");
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            logger.error("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        userService.deleteAll();
        logger.info("&" + Util.trace(Thread.currentThread().getStackTrace()) + " ***All users have been deleted");
        return new ResponseEntity(true, HttpStatus.NO_CONTENT);
    }

    @GetMapping("login")
    public boolean loginUser(@RequestParam String email, @RequestParam String password) {
        boolean dbUserIsExist = userService.authenticate(email, password);
       if(!dbUserIsExist) {
           logger.error("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Login information incorrect");
           return false;
       }
        logger.info("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Login successful");
        return true;
    }

    @PutMapping("updateBalance/{email}/{balance}")
    public boolean updateBalance(@PathVariable("email") String email, @PathVariable("balance")String balance){
        User dbUser = userService.findUserByEmail(email);
        if(dbUser == null) {
            logger.error("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return false;
        }
        dbUser.setBalance(dbUser.getBalance()+ Float.parseFloat(balance));
        userService.updateUser(dbUser);
        logger.info("#" + email + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***User balance updated. New balance : " + dbUser.getBalance());
        return true;
    }
}
