package com.sirius.web.controller;

import com.sirius.web.model.Automat;
import com.sirius.web.model.Bottle;
import com.sirius.web.service.AutomatService;
import com.sirius.web.service.BottleService;
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
    public ResponseEntity<List<Automat>> findAll() {
        final List<Automat> automats = automatService.getAllAutomats();

        if(automats == null) {
            logger.error("Kayitli otomatlar listenemedi");
            return new ResponseEntity<List<Automat>>(HttpStatus.NOT_FOUND);
        }
        logger.info("Kayitli otomat listesi: " + automats);
        return new ResponseEntity<List<Automat>>(automats, HttpStatus.OK);
    }

    @PostMapping("addAutomat")
    public ResponseEntity<Automat> createAutomat(@RequestBody Automat automat) {

        if (automat.getId() != null && automat.getId().length() != 0 && !automatService.exists(automat.getId())) {
            final Automat dbautomat = automatService.createAutomat(automat);
            logger.info("Yeni otomat olusturuldu. Otomat bilgileri: " + automat);
            return new ResponseEntity<Automat>(dbautomat, HttpStatus.CREATED);
        } else {
            logger.error("Yeni otomat olusturalamadi");
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Automat> getAutomat(@PathVariable String id) {
        final Automat dbAutomat = automatService.findAutomatById(id);
        if (dbAutomat != null) {
            logger.info(id + " ID numarali otomatin bilgileri: " + dbAutomat);
            return new ResponseEntity<Automat>(dbAutomat, HttpStatus.OK);
        } else {
            logger.error(id + " ID numarali otomat bulunamadi");
            return new ResponseEntity<Automat>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateAutomat")
    public ResponseEntity<Automat> updateAutomat(@RequestBody Automat automat) {

        final boolean isExist = automatService.exists(automat.getId());
        if (isExist) {
            automatService.updateAutomat(automat);
            logger.info(automat.getId() + " ID numarali otomatin bilgileri guncellenmistir");
            return new ResponseEntity<Automat>(automat, HttpStatus.OK);
        } else {
            logger.error(automat.getId() + " ID numarali otomat bulunamadi");
            return new ResponseEntity<Automat>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAutomat")
    public ResponseEntity deleteAutomat(@RequestParam String id){
        if(automatService.exists(id)) {
            automatService.deleteAutomat(id);
            logger.info(id + " ID numarali otomat silindi");
            return new ResponseEntity(true, HttpStatus.OK);
        } else {
            logger.error(id + " ID numarali otomat silinemedi");
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity deleteAll() {
        automatService.deleteAll();
        logger.info("Tüm otomatlar silindi");
        return new ResponseEntity(true, HttpStatus.NO_CONTENT);
    }

    @PutMapping("changeCapacity/{id}/{barcode}")
    public ResponseEntity changeCapacity(@PathVariable String id, @PathVariable String barcode) {

        boolean isDbAutomat = automatService.exists(id);
        boolean isDbBottle = bottleService.exists(barcode);

        if (!isDbAutomat || !isDbBottle) {
            logger.error("Otomatin ID numarasi veya sisenin barcode numarasi yanlis");
            return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
        }
        else {
            Automat getAutomatFromDb = automatService.findAutomatById(id);
            Bottle getBottleFromDb = bottleService.findBottleByBarcode(barcode);

            if(!getAutomatFromDb.isActive()) {
                logger.error(id + " ID numarali otomat aktif degil");
                return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
            }

            getAutomatFromDb.setNumberOfBottles(getAutomatFromDb.getNumberOfBottles() + 1);
            getAutomatFromDb.setCapacity(getAutomatFromDb.getCapacity() -
                    ( (getBottleFromDb.getVolume()/getAutomatFromDb.getOverallVolume()) * 100) );

            if(getAutomatFromDb.getCapacity() <= 100.00) {
                automatService.updateAutomat(getAutomatFromDb);
                logger.info(id + " ID numarali otomatin yeni kapasitesi: " + getAutomatFromDb.getCapacity());
                return new ResponseEntity(true, HttpStatus.OK);
            }
            else {
                getAutomatFromDb.setActive(false);
                automatService.updateAutomat(getAutomatFromDb);
                logger.error(id + " ID numarali otomatin kapasitesi doludur");
                return new ResponseEntity(false, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
