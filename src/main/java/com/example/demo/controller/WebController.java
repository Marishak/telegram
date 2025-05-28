package com.example.demo.controller;

import com.example.demo.core.dto.UserDto;
import com.example.demo.service.TelegramAuthService;
import com.example.demo.service.TelegramBotService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class WebController {

    final private TelegramBotService telegramBotService;
    final private TelegramAuthService telegramAuthService;
    final private UserService userService;

    public WebController(TelegramBotService telegramBotService,
                         TelegramAuthService telegramAuthService,
                         UserService userService) {
        this.telegramBotService = telegramBotService;
        this.telegramAuthService = telegramAuthService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @PostMapping("/send")
    public String sendToBot(@RequestParam String text, Model model) {
        model.addAttribute("message", "Вы написали: " + text);
        return "index";
    }

    @PostMapping("/webapp-auth")
    @ResponseBody
    public ResponseEntity<UserDto> authFromTelegram(@RequestParam String initData) {

        if (!telegramAuthService.checkTelegramAuthorization(initData)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto user = telegramAuthService.getUser(initData);

        if (!userService.isUserExists(user.getUserId())) {
            userService.save(user);
        }

        return ResponseEntity.ok(user);
    }
}