package com.kaibai.apiinterface.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kaibai
 * @date 2023-11-06 20:57
 **/
@RestController
public class MockController {

    @GetMapping("/name")
    public String getName(HttpServletRequest httpServletRequest) {
        System.out.println("请求");
        return "fuck you";
    }

    @PostMapping("/name")
    public String getNameByPost(@RequestParam String name) {
        return "Your name is" + name;
    }
}
