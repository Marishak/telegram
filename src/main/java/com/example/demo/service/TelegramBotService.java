package com.example.demo.service;

/**
 * Сервис работы с телеграм ботом
 */
public interface TelegramBotService {
    void sendMessage(String chatId, String text);
}
