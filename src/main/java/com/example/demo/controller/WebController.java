package com.example.demo.controller;

import com.example.demo.service.TelegramBotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    final private TelegramBotService telegramBotService;

    public WebController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Привет с Thymeleaf!");
        return "index";
    }

    @PostMapping("/send")
    public String sendToBot(@RequestParam String text, Model model) {
        // отображаем текст на странице, который ввели
        model.addAttribute("message", "Вы написали: " + text);
        return "index";
    }
}