package com.example.awstest.controller;

import com.example.awstest.service.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neevels
 * @version 1.0
 * @date 03.11.2023 14:10
 */
@RestController
public class Test {

    public final Service service;

    public Test(Service service) {
        this.service = service;
    }

    @GetMapping
    public String get() {
        return service.getFromAws();
    }
}
