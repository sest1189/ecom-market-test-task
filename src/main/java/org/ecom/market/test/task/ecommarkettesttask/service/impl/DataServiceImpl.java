package org.ecom.market.test.task.ecommarkettesttask.service.impl;

import org.ecom.market.test.task.ecommarkettesttask.annotations.RequestCounterRestriction;
import org.ecom.market.test.task.ecommarkettesttask.service.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataService {

    @RequestCounterRestriction(requestCount = "2",
            limitationTimePeriod = "20")
    @Override
    public ResponseEntity getData() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
