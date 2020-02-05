package com.sirius.web.controller;

import com.sirius.web.model.User;
import com.sirius.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
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
    public ResponseEntity<List<User>> findAll(){
        final List<User> users = userService.getAllUsers();

        if(users == null){
            return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PostMapping("addUser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try {
            final User dbuser = userService.createUser(user);
            return new ResponseEntity<User>(dbuser, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email){
        try {
            final User dbuser = userService.findUserByEmail(email);
            if(dbuser == null){
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user){
        try {
            final User dbuser = userService.updateUser(email, user);
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable String email, @RequestBody User user){
        try {
            final User dbuser = userService.deleteUser(email, user);
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

}
