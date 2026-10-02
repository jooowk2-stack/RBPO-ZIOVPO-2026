package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @GetMapping("/text")
    public String getText() {
        return "Привет! Это ответ первого эндпоинта.";
    }

    @GetMapping("/numbers")
    public List<Integer> getNumbers() {
        return List.of(10, 20, 30, 40, 50);
    }
}