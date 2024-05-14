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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class PromoCodeController {
    private final PromoCodeService promoCodeService;
    private final Mapper<PromoCodeEntity, PromoCodeDto> promoCodeMapper;

    public PromoCodeController(PromoCodeService promoCodeService, Mapper<PromoCodeEntity, PromoCodeDto> promoCodeMapper) {
        this.promoCodeService = promoCodeService;
        this.promoCodeMapper = promoCodeMapper;
    }

    //TODO: ADD VALIDATION FOR PROMO CODE (for example if expiration date is not before today, if price is not below 0)

    @PostMapping(path = "/promoCode")
    public ResponseEntity<PromoCodeDto> createPromoCode(@RequestBody PromoCodeDto promoCodeDto) {
        checkPromoCodeDtoValidation(promoCodeDto);

        PromoCodeEntity promoCodeEntity = promoCodeMapper.mapFrom(promoCodeDto);

        if (promoCodeService.existByPromoCode(promoCodeEntity.getPromoCode())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Promo code already exist"
            );
        }

        PromoCodeEntity savedPromoCodeEntity = promoCodeService.save(promoCodeEntity);
        return new ResponseEntity<>(
                promoCodeMapper.mapTo(savedPromoCodeEntity),
                HttpStatus.CREATED
        );
    }

    @GetMapping(path = "/promoCode/{promoCode}")
    public ResponseEntity<PromoCodeDto> getOnePromoCode(@PathVariable("promoCode") String promoCode) {
        Optional<PromoCodeEntity> foundPromoCode = promoCodeService.findOne(promoCode);

        return foundPromoCode.map(PromoCodeEntity -> {
            PromoCodeDto promoCodeDto = promoCodeMapper.mapTo(PromoCodeEntity);
            return new ResponseEntity<>(
                    promoCodeDto,
                    HttpStatus.OK
            );
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping(path = "/promoCode")
    public Page<PromoCodeDto> getAllPromoCodes(Pageable pageable) {
        Page<PromoCodeEntity> allFoundPromoCodes = promoCodeService.findAll(pageable);
        return allFoundPromoCodes.map(promoCodeMapper::mapTo);
    }

    @PutMapping(path = "/promoCode/{promoCode}")
    public ResponseEntity<PromoCodeDto> fullUpdatePromoCode(
            @PathVariable("promoCode") String promoCode,
            @RequestBody PromoCodeDto promoCodeDto
    ) {
        return getPromoCodeDtoResponseEntity(promoCode, promoCodeDto);
    }

    @PatchMapping(path = "/promoCode/{promoCodeId}")
    public ResponseEntity<PromoCodeDto> partialUpdatePromoCode(
            @PathVariable("promoCodeId") String promoCode,
            @RequestBody PromoCodeDto promoCodeDto
    ) {
        return getPromoCodeDtoResponseEntity(promoCode, promoCodeDto);
    }

    private ResponseEntity<PromoCodeDto> getPromoCodeDtoResponseEntity(
            @PathVariable("promoCodeId") String promoCode,
            @RequestBody PromoCodeDto promoCodeDto
    ) {
        checkPromoCodeExistence(promoCode);

        //If and only if promo code discount amount is present, check if it's below 0
        Optional<BigDecimal> promoCodeDiscountAmount = Optional.ofNullable(promoCodeDto.getPromoCodeDiscountAmount());
        promoCodeDiscountAmount.ifPresent(this::isPromoCodeDiscountAmountBelowZero);

        PromoCodeEntity promoCodeEntity = promoCodeMapper.mapFrom(promoCodeDto);
        PromoCodeEntity savedPromoCodeentity = promoCodeService.updatePromoCode(promoCode, promoCodeEntity);

        return new ResponseEntity<>(
                promoCodeMapper.mapTo(savedPromoCodeentity),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/promoCode/{promoCode}")
    public ResponseEntity<Void> deletePromoCode(@PathVariable("promoCode") String promoCode) {
        checkPromoCodeExistence(promoCode);

        promoCodeService.deleteByPromoCode(promoCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkPromoCodeExistence(String promoCode) {
        if (promoCodeService.findOne(promoCode).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Promo code not found."
            );
        }
    }

    private void checkPromoCodeDtoValidation(PromoCodeDto promoCodeDto) {

        final String regexPattern = "^[a-zA-Z0-9]{3,24}$";

        final Pattern pattern = Pattern.compile(regexPattern);
        final Matcher matcher = pattern.matcher(promoCodeDto.getPromoCode());

        if (!matcher.matches()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Invalid promo code"
            );
        }

        isPromoCodeDiscountAmountBelowZero(promoCodeDto.getPromoCodeDiscountAmount());

        //Check if promo code expiration date is before today
        if (promoCodeDto.getPromoCodeExpirationDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Wrong expiration date"
            );
        }

        if (promoCodeDto.getPromoCodeAllowedUsagesNumber() <= 0){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Promo code allowed usages is below or equal to 0"
            );
        }
    }

    private void isPromoCodeDiscountAmountBelowZero(BigDecimal productPrice) {
        if(productPrice.compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Promo code discount amount is below 0"
            );
        }
    }

}
