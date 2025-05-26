package com.example.demo.controller;

import com.example.demo.service.TelegramBotService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    //bmm эти сервисы нужны будут в будущем, подумать разделении контроллера
    final private TelegramBotService telegramBotService;
    final private UserService userService;

    public WebController(TelegramBotService telegramBotService,
                         UserService userService) {
        this.telegramBotService = telegramBotService;
        this.userService = userService;
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