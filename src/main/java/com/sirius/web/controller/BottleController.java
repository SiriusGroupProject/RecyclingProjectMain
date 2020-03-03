package com.sirius.web.controller;

import com.sirius.web.model.Bottle;
import com.sirius.web.service.BottleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/rest/bottles")
public class BottleController implements Serializable {

    private final BottleService bottleService;

    @Autowired
    public BottleController(BottleService bottleService) {
        this.bottleService = bottleService;
    }

    @GetMapping("listBottles")
    public ResponseEntity<List<Bottle>> findAll() {
        final List<Bottle> bottles = bottleService.getAllBottles();

        if(bottles == null) {
            return new ResponseEntity<List<Bottle>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<Bottle>>(bottles, HttpStatus.OK);
    }

    @PostMapping("addBottle")
    public ResponseEntity<Bottle> createBottle(@RequestBody Bottle bottle) {

        if (bottle.getBarcode() != null && bottle.getBarcode().length() != 0 && !bottleService.exists(bottle.getBarcode())) {
            final Bottle dbBottle = bottleService.createBottle(bottle);
            return new ResponseEntity<Bottle>(dbBottle, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Bottle>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{barcode}")
    public ResponseEntity<Bottle> getBottle(@PathVariable String barcode) {
        try {
            final Bottle dbBottle = bottleService.findBottleByBarcode(barcode);
            if (dbBottle == null) {
                return new ResponseEntity<Bottle>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Bottle>(dbBottle, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Bottle>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateBottle")
    public ResponseEntity<Bottle> updateBottle(@RequestBody Bottle bottle) {

        final boolean isExist = bottleService.exists(bottle.getBarcode());
        if (isExist) {
            bottleService.updateBottle(bottle);
            return new ResponseEntity<Bottle>(bottle, HttpStatus.OK);
        } else {
            return new ResponseEntity<Bottle>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteBottle")
    public ResponseEntity deleteBottle(@RequestParam String barcode){
        try {
            boolean ok = bottleService.deleteBottle(barcode);
            return new ResponseEntity(ok, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity(false, HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        boolean ok = bottleService.deleteAll();
        return new ResponseEntity(ok, HttpStatus.NO_CONTENT);
    }

}
