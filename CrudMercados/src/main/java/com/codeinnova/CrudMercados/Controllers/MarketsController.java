package com.codeinnova.CrudMercados.Controllers;

import com.codeinnova.CrudMercados.Entities.Markets;
import com.codeinnova.CrudMercados.Repository.MarketsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MarketsController {

    private final Logger log = LoggerFactory.getLogger(MarketsController.class); //Creando el log

    private MarketsRepository marketsRepository; // Atributo

    public MarketsController(MarketsRepository marketsRepository) {
        this.marketsRepository = marketsRepository;
    }

//CRUD

    // Search all

    @GetMapping("/api/markets")
    public List<Markets> findAll(){
        //Recuperar y devolver los mercados de la DB
        return marketsRepository.findAll();
    }
    // Search one for ID

    @GetMapping("api/markets/{id}")
    public ResponseEntity<Markets> findOneById(@PathVariable Long id) {

        Optional<Markets> marketOptional = marketsRepository.findById(id);

        if (marketOptional.isPresent())
            return ResponseEntity.ok(marketOptional.get());
        else
            return ResponseEntity.notFound().build();
    }
    // Create market in DB

    @PostMapping("/api/markets")
    public ResponseEntity<Markets> create(@RequestBody Markets market, @RequestHeader HttpHeaders headers){
        System.out.println(headers.get("User-Agent"));
        if(market.getId() != null){ //Verificar el ID para ver si ya existe y proceder a crearlo
            log.warn("Trying to create a market with id"); //Para que los errores se guarden en un archivo específico
            System.out.println("Trying to create a market with id");
            return ResponseEntity.badRequest().build();
        }
        Markets result = marketsRepository.save(market);
        return ResponseEntity.ok(result);
    }

    // Update market in DB

    @PutMapping("/api/markets")
    public ResponseEntity<Markets> update(@RequestBody Markets market){
        if(market.getId() ==null){
            log.warn("Trying to update a non existent market");
            return ResponseEntity.badRequest().build();
        }
        if(!marketsRepository.existsById(market.getId())) { //Existia y ya no
            log.warn("Trying to update a non existent market");
            return ResponseEntity.notFound().build();
        }
        Markets result = marketsRepository.save(market);
        return ResponseEntity.ok(result);

    }

    // Delete

    @DeleteMapping("/api/markets/{id}")
    public ResponseEntity<Markets> delete(@PathVariable Long id){

        if(!marketsRepository.existsById(id)){ //Existia y ya no
            log.warn("Trying to delete a non existent market");
            return ResponseEntity.notFound().build();
        }

        marketsRepository.deleteById(id);

        return ResponseEntity.noContent().build(); //Devuelve que el contenido ya no está disponible

    }

}