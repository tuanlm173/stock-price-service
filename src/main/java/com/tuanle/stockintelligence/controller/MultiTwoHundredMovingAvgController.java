package com.tuanle.stockintelligence.controller;

import com.tuanle.stockintelligence.dto.MultiTwoHundredMovingAvg;
import com.tuanle.stockintelligence.service.MultiMovingAverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultiTwoHundredMovingAvgController {

    private final MultiMovingAverageService multiMovingAverageService;

    @Autowired
    public MultiTwoHundredMovingAvgController(MultiMovingAverageService multiMovingAverageService) {
        this.multiMovingAverageService = multiMovingAverageService;
    }

    @GetMapping(path = "/api/v2/multi200dma", produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(value = "multiplemovingaverage")
    public ResponseEntity<MultiTwoHundredMovingAvg> getMulti200MovingAvg(@RequestParam(value = "tickers") String tickersParam,
                                                                    @RequestParam(value = "start-date") String startDateParam) {

        MultiTwoHundredMovingAvg result = multiMovingAverageService.getMultiMovingAverageForStocks(tickersParam, startDateParam);
        return ResponseEntity.ok().body(result);
    }

}
