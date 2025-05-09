package com.ybb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/auth")
    public String test() {
        return "test";
    }

    @GetMapping("/no_auth")
    public String noAuth() {
        return "no_auth";
    }
}
