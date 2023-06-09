package com.bike.shop.controller;

import com.bike.shop.dto.BikeOrderDto;
import com.bike.shop.model.BikeModel;
import com.bike.shop.service.BikeService;
import com.bike.shop.service.ConvertBikeService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/bike")
public class BikeController {
    @Autowired
    public BikeService bikeService;
    @Autowired
    public ConvertBikeService convertBikeService;
    public BikeModel bikeModel;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid BikeOrderDto bikeOrderDto) {
        try {
            bikeModel = bikeService.saveBike(convertBikeService.bikeEntity(bikeOrderDto));
            log.info("BIKE-CONTROLLER = bike registrada");
        } catch (Exception e) {
            log.info("BIKE-CONTROLLER = erro ao registrar bike");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CAMPO NAO PODE SER VAZIO OU NULO");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(convertBikeService.bikeDto(bikeModel));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            BikeModel bikeId = bikeService.findBikeId(id);
            bikeService.deleteBike(bikeId);
            log.info("BIKE-CONTROLLER = bike removida com sucesso");
        } catch (DataIntegrityViolationException e) {
            log.info("BIKE-CONTROLLER = erro ao remover bike foreignkey");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BICICLETA NAO PODE SER REMOVIDA POIS ESTA EM USO");
        } catch (Exception e) {
            log.info("BIKE-CONTROLLER = bike nao existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BICICLETA NAO EXISTE");
        }
        return ResponseEntity.status(HttpStatus.OK).body("BICICLETA REMOVIDA");
    }

    @GetMapping("/findid/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        BikeModel cod;
        try {
            cod = bikeService.findBikeId(id);
            log.info("BIKE-CONTROLLER = retorna bike por id");
        } catch (Exception e) {
            log.info("BIKE-CONTROLLER = erro ao buscar bike por id, nao existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BICICLETA NAO EXISTE");
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertBikeService.bikeDto(cod));
    }

    @GetMapping("/findall")
    public ResponseEntity<?> findAll() {
        List<BikeModel> cod;
        try {
            cod = bikeService.all();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BICICLETA NAO EXISTE");
        }
        return ResponseEntity.status(HttpStatus.OK).body(cod);
    }
}