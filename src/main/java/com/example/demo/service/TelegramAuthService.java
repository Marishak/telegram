package com.example.demo.service;

import com.example.demo.core.dto.UserDto;

/**
 * Сервис для работы с аутентификацией через telegram
 */
public interface TelegramAuthService {
    boolean checkTelegramAuthorization(String initData);

    UserDto getUser(String initData);
}
