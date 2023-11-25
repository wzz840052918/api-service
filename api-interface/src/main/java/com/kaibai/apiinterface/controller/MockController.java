package com.kaibai.apiinterface.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kaibai
 * @date 2023-11-06 20:57
 **/
@RestController
public class MockController {

    @GetMapping("/name")
    public String getName(@RequestParam String name, HttpServletRequest httpServletRequest) {
        return "fuck you";
    }

    @PostMapping("/name")
    public String getNameByPost(@RequestParam String name) {
        return "Your name is" + name;
    }
}
