package com.sirius.web.controller;

import com.sirius.web.model.Bottle;
import com.sirius.web.service.BottleService;
import com.sirius.web.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/rest/bottles")
public class BottleController implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(BottleController.class);
    private final BottleService bottleService;

    @Autowired
    public BottleController(BottleService bottleService) {
        this.bottleService = bottleService;
    }

    @GetMapping("listBottles")
    public ResponseEntity<List<Bottle>> findAllBottles() {
        final List<Bottle> bottles = bottleService.getAllBottles();

        if(bottles == null) {
            logger.error("&" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Registered bottles could not be listed");
            return new ResponseEntity<List<Bottle>>(HttpStatus.NOT_FOUND);
        }
        logger.info("&" + Util.trace(Thread.currentThread().getStackTrace()) + " ***List of registered automats : " + bottles);
        return new ResponseEntity<List<Bottle>>(bottles, HttpStatus.OK);
    }

    @PostMapping("addBottle")
    public ResponseEntity<Bottle> createBottle(@RequestBody Bottle bottle) {

        if (bottle.getBarcode() != null && bottle.getBarcode().length() != 0 && !bottleService.exists(bottle.getBarcode())) {
            final Bottle dbBottle = bottleService.createBottle(bottle);
            logger.info("%" + bottle.getBarcode() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***New bottle has been created. Information of the new bottle : " + bottle);
            return new ResponseEntity<Bottle>(dbBottle, HttpStatus.CREATED);
        } else {
            logger.error("%" + bottle.getBarcode() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Failed to create new bottle");
            return new ResponseEntity<Bottle>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{barcode}")
    public ResponseEntity<Bottle> getBottle(@PathVariable String barcode) {
        final Bottle dbBottle = bottleService.findBottleByBarcode(barcode);
        if (dbBottle != null) {
            logger.info("%" + barcode + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Bottle information : " + dbBottle);
            return new ResponseEntity<Bottle>(dbBottle, HttpStatus.OK);
        } else {
            logger.error("%" + barcode + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Bottle information not found");
            return new ResponseEntity<Bottle>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateBottle")
    public ResponseEntity<Bottle> updateBottle(@RequestBody Bottle bottle) {

        final boolean isExist = bottleService.exists(bottle.getBarcode());
        if (isExist) {
            bottleService.updateBottle(bottle);
            logger.info("%" + bottle.getBarcode() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Bottle information has been updated");
            return new ResponseEntity<Bottle>(bottle, HttpStatus.OK);
        } else {
            logger.error("%" + bottle.getBarcode() + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity<Bottle>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteBottle")
    public ResponseEntity deleteBottle(@RequestParam String barcode){
        if(bottleService.exists(barcode)) {
            bottleService.deleteBottle(barcode);
            logger.info("%" + barcode + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***The bottle has been deleted");
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            logger.error("%" + barcode + " &" + Util.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        bottleService.deleteAll();
        logger.info("&" + Util.trace(Thread.currentThread().getStackTrace()) + " ***All bottles have been deleted");
        return new ResponseEntity(true, HttpStatus.NO_CONTENT);
    }

}
