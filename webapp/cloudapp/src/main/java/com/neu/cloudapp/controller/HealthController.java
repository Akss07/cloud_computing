package com.neu.cloudapp.controller;

import com.neu.cloudapp.service.DocumentService;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final static Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    StatsDClient statsDClient;
    @RequestMapping( value = "/healthz", method = RequestMethod.GET)
    public ResponseEntity health(){
        logger.info("Inside Health Controller");
        statsDClient.increment("APIGetHealthCount");
        return ResponseEntity.ok(null);
    }
}
