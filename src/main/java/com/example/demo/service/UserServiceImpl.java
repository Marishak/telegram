package com.example.demo.service;

import com.example.demo.core.dto.UserDto;
import com.example.demo.core.entity.User;
import com.example.demo.core.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long save(UserDto dto) {
        return userRepository.save(User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .userId(dto.getUserId())
                .build()).getId();
    }

    @Override
    public boolean isUserExists(Long userId) {
        return userRepository.findByUserId(userId).isPresent();
    }
}
