package com.sirius.web.controller;

import com.sirius.web.model.Automat;
import com.sirius.web.service.AutomatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/rest/automats")
public class AutomatController implements Serializable {

    private final AutomatService automatService;

    @Autowired
    public AutomatController(AutomatService automatService) {
        this.automatService = automatService;
    }

    @GetMapping("listAutomats")
    public ResponseEntity<List<Automat>> findAll(){
        final List<Automat> automats = automatService.getAllAutomats();

        if(automats == null){
            return new ResponseEntity<List<Automat>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Automat>>(automats, HttpStatus.OK);
    }

    @PostMapping("addAutomat")
    public ResponseEntity<Automat> createAutomat(@RequestBody Automat automat){
        try {
            final Automat dbautomat = automatService.createAutomat(automat);
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Automat> getUser(@PathVariable String id){
        try {
            final Automat dbautomat = automatService.findAutomatById(id);
            if(dbautomat == null){
                return new ResponseEntity<Automat>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Automat>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Automat> updateUser(@PathVariable String id, @RequestBody Automat automat){
        try {
            final Automat dbautomat = automatService.updateAutomat(id, automat);
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Automat> deleteUser(@PathVariable String id){
        try {
            final Automat dbautomat = automatService.deleteAutomat(id);
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<Automat>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }
    }

}
