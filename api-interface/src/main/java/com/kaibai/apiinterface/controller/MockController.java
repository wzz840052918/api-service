package com.kaibai.apiinterface.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kaibai
 * @date 2023-11-06 20:57
 **/
@RestController
@RequestMapping("/name")
public class MockController {

    @GetMapping("/")
    public String getName(@RequestParam String name, HttpServletRequest httpServletRequest) {
        return "fuck you";
    }

    @PostMapping("/name")
    public String getNameByPost(@RequestParam String name) {
        return "Your name is" + name;
    }
}
