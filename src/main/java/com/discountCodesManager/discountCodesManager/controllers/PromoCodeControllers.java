package com.discountCodesManager.discountCodesManager.controllers;

import com.discountCodesManager.discountCodesManager.domain.dto.PromoCodeDto;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class PromoCodeControllers {
    private final PromoCodeService promoCodeService;
    private final Mapper<PromoCodeEntity, PromoCodeDto> promoCodeMapper;

    public PromoCodeControllers(PromoCodeService promoCodeService, Mapper<PromoCodeEntity, PromoCodeDto> promoCodeMapper) {
        this.promoCodeService = promoCodeService;
        this.promoCodeMapper = promoCodeMapper;
    }

    //TODO: ADD VALIDATION FOR PROMO CODE (for example if expiration date is not before today, if price is not below 0, CHECK PROMO CODE ID VALIDATION!)

    @PostMapping(path = "/promoCode")
    public ResponseEntity<PromoCodeDto> createPromoCode(@RequestBody PromoCodeDto promoCodeDto) {
        PromoCodeEntity promoCodeEntity = promoCodeMapper.mapFrom(promoCodeDto);

        if(promoCodeService.existByPromoCode(promoCodeDto.getPromoCode())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Promo code already exists"
            );
        }

        checkPromoCodeValidation(promoCodeDto.getPromoCode());

        PromoCodeEntity savedPromoCodeEntity = promoCodeService.save(promoCodeEntity);
        return new ResponseEntity<>(
                promoCodeMapper.mapTo(savedPromoCodeEntity),
                HttpStatus.CREATED
        );
    }

    @GetMapping(path = "/promoCode/{promoCode}")
    public ResponseEntity<PromoCodeDto> getPromoCodeById(@PathVariable("promoCode") String promoCode) {
        Optional<PromoCodeEntity> foundPromoCode = promoCodeService.findOne(promoCode);

        return foundPromoCode.map(PromoCodeEntity -> {
            PromoCodeDto promoCodeDto = promoCodeMapper.mapTo(PromoCodeEntity);
            return new ResponseEntity<>(promoCodeDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

//    @PostMapping(path = "/promoCode/{promoCode}")
//    public ResponseEntity<PromoCodeDto> getPromoCodeByPromoCode(@PathVariable("promoCode") String promoCode){
//        Optional<PO>
//    }

    @GetMapping(path = "/promoCode")
    public Page<PromoCodeDto> getAllPromoCodes(Pageable pageable){
        Page<PromoCodeEntity> allFoundPromoCodes = promoCodeService.findAll(pageable);

        return allFoundPromoCodes.map(promoCodeMapper::mapTo);
    }

    @PutMapping(path = "/promoCode/{promoCode}")
    public ResponseEntity<PromoCodeDto> fullUpdatePromoCode(
            @PathVariable("promoCode") String promoCode,
            @RequestBody PromoCodeDto promoCodeDto
    ){

        //TODO: check if promoCode exist

        checkPromoCodeExistence(promoCode);

        //promoCodeDto.setPromoCode(promoCode);

        PromoCodeEntity promoCodeEntity = promoCodeMapper.mapFrom(promoCodeDto);
        PromoCodeEntity savedPromoCodeEntity = promoCodeService.updatePromoCode(promoCode, promoCodeEntity);

        return new ResponseEntity<>(promoCodeMapper.mapTo(savedPromoCodeEntity),HttpStatus.OK);
    }

    @PatchMapping(path = "/promoCode/{promoCodeId}")
    public ResponseEntity<PromoCodeDto> partialUpdatePromoCode(
            @PathVariable("promoCodeId") String promoCode,
            @RequestBody PromoCodeDto promoCodeDto
    ){
        checkPromoCodeExistence(promoCode);

        PromoCodeEntity promoCodeEntity = promoCodeMapper.mapFrom(promoCodeDto);
        PromoCodeEntity savedPromoCodeentity = promoCodeService.updatePromoCode(promoCode, promoCodeEntity);

        return new ResponseEntity<>(promoCodeMapper.mapTo(savedPromoCodeentity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/promoCode/{promoCode}")
    public ResponseEntity<Void> deletePromoCode(@PathVariable("promoCode") String promoCode){
        checkPromoCodeExistence(promoCode);

        promoCodeService.deleteById(promoCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkPromoCodeExistence(String promoCode){
        if(promoCodeService.findOne(promoCode).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promo code not found.");
        }
    }

    private void checkPromoCodeValidation(String promoCode){
        final String regexPattern = "^[a-zA-Z0-9]{3,24}$";

        final Pattern pattern = Pattern.compile(regexPattern);
        final Matcher matcher = pattern.matcher(promoCode);

        if(!matcher.matches()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid promo code");
        }
    }

}
