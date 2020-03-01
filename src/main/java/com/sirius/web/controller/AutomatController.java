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
    public ResponseEntity<List<Automat>> findAll() {
        final List<Automat> automats = automatService.getAllAutomats();

        if(automats == null) {
            return new ResponseEntity<List<Automat>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Automat>>(automats, HttpStatus.OK);
    }

    @PostMapping("addAutomat")
    public ResponseEntity<Automat> createAutomat(@RequestBody Automat automat) {

        if (automat.getId() != null && automat.getId().length() != 0 && !automatService.exists(automat.getId())) {
            final Automat dbautomat = automatService.createAutomat(automat);
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Automat> getAutomat(@PathVariable String id) {
        try {
            final Automat dbautomat = automatService.findAutomatById(id);
            if (dbautomat == null) {
                return new ResponseEntity<Automat>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Automat>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateAutomat")
    public ResponseEntity<Automat> updateAutomat(@RequestBody Automat automat) {

        final boolean isExist = automatService.exists(automat.getId());
        if (isExist) {
            automatService.updateAutomat(automat);
            return new ResponseEntity<Automat>(automat, HttpStatus.OK);
        } else {
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAutomat")
    public ResponseEntity deleteAutomat(@RequestParam String id){
        try {
            boolean ok = automatService.deleteAutomat(id);
            return new ResponseEntity(ok, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity(false, HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        boolean ok = automatService.deleteAll();
        return new ResponseEntity(ok, HttpStatus.NO_CONTENT);
    }
}
