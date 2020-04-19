package com.sirius.web.controller;

import com.sirius.web.model.Automat;
import com.sirius.web.model.Bottle;
import com.sirius.web.service.AutomatService;
import com.sirius.web.service.BottleService;
import com.sirius.web.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/rest/automats")
public class AutomatController implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(AutomatController.class);
    private final AutomatService automatService;
    private final BottleService bottleService;

    @Autowired
    public AutomatController(AutomatService automatService, BottleService bottleService) {
        this.automatService = automatService;
        this.bottleService = bottleService;
    }

    @GetMapping("listAutomats")
    public ResponseEntity<List<Automat>> findAllAutomats() {
        final List<Automat> automats = automatService.getAllAutomats();

        if(automats == null) {
            logger.error("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Registered automats could not be listed");
            return new ResponseEntity<List<Automat>>(HttpStatus.NOT_FOUND);
        }
        logger.info("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***List of registered automats : " + automats);
        return new ResponseEntity<List<Automat>>(automats, HttpStatus.OK);
    }

    @PostMapping("addAutomat")
    public ResponseEntity<Automat> createAutomat(@RequestBody Automat automat) {

        if (automat.getId() != null && automat.getId().length() != 0 && !automatService.exists(automat.getId())) {
            final Automat dbautomat = automatService.createAutomat(automat);
            logger.info("$" + dbautomat.getId() + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***New automat has been created. Information of the new automat : " + dbautomat);
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.CREATED);
        } else {
            logger.error("$" + automat.getId() + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Failed to create new automat");
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Automat> getAutomat(@PathVariable String id) {
        final Automat dbAutomat = automatService.findAutomatById(id);
        if (dbAutomat != null) {
            logger.info("$" + id + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat information : " + dbAutomat);
            return new ResponseEntity<Automat>(dbAutomat, HttpStatus.OK);
        } else {
            logger.error("$" + id + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat information not found");
            return new ResponseEntity<Automat>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateAutomat")
    public ResponseEntity<Automat> updateAutomat(@RequestBody Automat automat) {

        final boolean isExist = automatService.exists(automat.getId());
        if (isExist) {
            automatService.updateAutomat(automat);
            logger.info("$" + automat.getId() + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat information has been updated");
            return new ResponseEntity<Automat>(automat, HttpStatus.OK);
        } else {
            logger.error("$" + automat.getId() + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAutomat")
    public ResponseEntity deleteAutomat(@RequestParam String id){
        if(automatService.exists(id)) {
            automatService.deleteAutomat(id);
            logger.info("$" + id + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The automat has been deleted");
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            logger.error("$" + id + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        automatService.deleteAll();
        logger.info("&" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***All automats have been deleted");
        return new ResponseEntity(true, HttpStatus.NO_CONTENT);
    }

    @PutMapping("changeCapacity/{id}/{barcode}")
    public ResponseEntity changeCapacity(@PathVariable String id, @PathVariable String barcode) {

        boolean isDbAutomat = automatService.exists(id);
        boolean isDbBottle = bottleService.exists(barcode);

        if (!isDbAutomat || !isDbBottle) {
            logger.error("$" + id + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Not found in database");
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
        else {
            Automat getAutomatFromDb = automatService.findAutomatById(id);
            Bottle getBottleFromDb = bottleService.findBottleByBarcode(barcode);

            if(!getAutomatFromDb.isActive()) {
                logger.error("$" + id + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat is deactive");
                return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
            }

            getAutomatFromDb.setNumberOfBottles(getAutomatFromDb.getNumberOfBottles() + 1);
            getAutomatFromDb.setCapacity(getAutomatFromDb.getCapacity() -
                    ( (getBottleFromDb.getVolume()/getAutomatFromDb.getOverallVolume()) * 100) );

            if(getAutomatFromDb.getCapacity() <= 100.00) {
                automatService.updateAutomat(getAutomatFromDb);
                logger.info("$" + id + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***The new capacity of Automat : " + getAutomatFromDb.getCapacity());
                return new ResponseEntity(true, HttpStatus.OK);
            }
            else {
                getAutomatFromDb.setActive(false);
                automatService.updateAutomat(getAutomatFromDb);
                logger.error("$" + id + " %" + barcode + " &" + Utility.trace(Thread.currentThread().getStackTrace()) + " ***Automat's capacity is full");
                return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
