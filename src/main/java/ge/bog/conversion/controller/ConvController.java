package ge.bog.conversion.controller;

import ge.bog.conversion.model.ConversionDto;
import ge.bog.conversion.model.CreateConvDto;
import ge.bog.conversion.service.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/conversion")
public class ConvController {

    public ConvController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    private ConversionService conversionService;

    @PostMapping
    ResponseEntity<ConversionDto> createOperation(@Valid @RequestBody CreateConvDto createConvDto) {
        ConversionDto response = conversionService.createOperation(createConvDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{user}/conv/{id}")
    ResponseEntity<Void> deleteOperation(@PathVariable String user, @PathVariable Long id) {
        conversionService.deleteOperation(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ConversionDto> getConversion(@PathVariable Long id) {
        ConversionDto response = conversionService.getConversion(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
