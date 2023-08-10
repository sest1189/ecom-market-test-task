package org.ecom.market.test.task.ecommarkettesttask.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecom.market.test.task.ecommarkettesttask.annotations.RequestCounterRestriction;
import org.ecom.market.test.task.ecommarkettesttask.service.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class MainController {
    private final DataService dataService;


    @RequestCounterRestriction(requestCount = "ecom.market.property.requests.limit",
            limitationTimePeriod = "ecom.market.property.limitation.time.period")
    @GetMapping(value = "/data")
    public ResponseEntity getData(HttpServletRequest request) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/metadata")
    public ResponseEntity getMetaData() {
        return dataService.getData();
    }
}
