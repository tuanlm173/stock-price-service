package com.tuanle.stockintelligence.controller;

import com.tuanle.stockintelligence.dto.TwoHundredMovingAvg;
import com.tuanle.stockintelligence.service.MovingAverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TwoHundredMovingAvgController {

    private final MovingAverageService movingAverageService;

    @Autowired
    public TwoHundredMovingAvgController(MovingAverageService movingAverageService) {
        this.movingAverageService = movingAverageService;
    }

    @GetMapping(path = "/api/v2/{ticker}/200dma", produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(value = "movingaverage")
    public ResponseEntity<TwoHundredMovingAvg> get200DayMovingAvg(@PathVariable(value = "ticker") String ticker,
                                                                  @RequestParam(value = "start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDateParam) throws IOException {

        TwoHundredMovingAvg result = movingAverageService.getMovingAverageForStock(ticker, startDateParam);
        return ResponseEntity.ok().body(result);
    }

}
