package com.example.demo.service;

/**
 * Сервис для работы с телеграм ботом
 */
public interface TelegramBotService {
    void sendMessage(String chatId, String text);
}
