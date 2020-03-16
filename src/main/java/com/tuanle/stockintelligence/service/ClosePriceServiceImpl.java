package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.dto.ClosePrice;
import com.tuanle.stockintelligence.dto.Price;
import com.tuanle.stockintelligence.repository.ClosePriceRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClosePriceServiceImpl implements ClosePriceService {

    private ClosePriceRepository closePriceRepository;

    @Autowired
    public ClosePriceServiceImpl(ClosePriceRepository closePriceRepository) {
        this.closePriceRepository = closePriceRepository;
    }

    @Override
    public List<List<String>> getListClosePrice(String ticker, String startDate, String endDate) {
        closePriceRepository.getClosePrice(ticker, startDate, endDate);
        JSONArray dataArray = closePriceRepository.getClosePriceJSONArray();
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(dataArray.iterator(), Spliterator.ORDERED), true)
                .map(JSONArray.class::cast)
                .map(closePrice -> Arrays.asList(closePrice.getString(0), String.valueOf(closePrice.getDouble(1))))
                .collect(Collectors.toList());
    }

    @Override
    public ClosePrice getTickerAndClosePrice(String ticker, String startDate, String endDate) {
        List<List<String>> listClosePrice = getListClosePrice(ticker, startDate, endDate);
        Price price = Price.of(ticker, listClosePrice);
        return ClosePrice.of(price);
    }


}
