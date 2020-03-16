package com.tuanle.stockintelligence.controller;

import com.tuanle.stockintelligence.dto.ClosePrice;
import com.tuanle.stockintelligence.service.ClosePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClosePriceController {

    private final ClosePriceService closePriceService;

    @Autowired
    public ClosePriceController(ClosePriceService closePriceService) {
        this.closePriceService = closePriceService;
    }

    @GetMapping(path = "/api/v2/{ticker}/closePrice", produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(value = "closeprice")
    public ResponseEntity<ClosePrice> getClosePrice(@PathVariable(value = "ticker") String ticker,
                                                    @RequestParam(value = "start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
                                                    @RequestParam(value = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        ClosePrice result = closePriceService.getTickerAndClosePrice(ticker, startDate, endDate);
        return ResponseEntity.ok().body(result);
    }
}
