package ru.mfa.rbpo_2026;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Mycontroller {
    @GetMapping("/text")
    public String getText() {
        return "Привет, лабораторная работа готова!";
    }
    @GetMapping("/number")
    public int getNumber() {
        return 24058;
    }
}