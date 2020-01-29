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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id){
        try {
            final User dbuser = userService.findUserById(id);
            if(dbuser == null){
                return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user){
        try {
            final User dbuser = userService.updateUser(id, user);
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id, @RequestBody User user){
        try {
            final User dbuser = userService.deleteUser(id, user);
            return new ResponseEntity<User>(dbuser, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

}
