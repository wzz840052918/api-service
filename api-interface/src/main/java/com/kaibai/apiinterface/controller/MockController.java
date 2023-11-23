package com.kaibai.apiinterface.controller;

import com.kaibai.apiinterface.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author kaibai
 * @date 2023-11-06 20:57
 **/
@RestController
public class MockController {

    @GetMapping("/name")
    public String getName(@RequestParam String name) {
        return "Your name is" + name;
    }

    @PostMapping("/name")
    public String getNameByPost(@RequestParam String name) {
        return "Your name is" + name;
    }

    @PostMapping("/named")
    public String getNamed(@RequestBody User user) {
        return "Your name is" + user.getUsername();
    }
}
