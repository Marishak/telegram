package com.example.demo.service;

import com.example.demo.core.dto.UserDto;

/**
 * Сервис работы с пользователем
 */
public interface UserService {
    Long save(UserDto user);

    boolean isUserExists(Long userId);
}
