package com.example.demo.service;

import com.example.demo.bot.TelegramBot;
import com.example.demo.core.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TelegramAuthServiceImpl implements TelegramAuthService {

    private final TelegramBot telegramBot;
    private final ObjectMapper objectMapper;

    public TelegramAuthServiceImpl(TelegramBot telegramBot,
                                   ObjectMapper objectMapper) {
        this.telegramBot = telegramBot;
        this.objectMapper = objectMapper;
    }

//    Data-check-string — это цепочка всех полученных полей,
//    1. отсортированных в алфавитном порядке,
//    2. в формате, key=<value>
//    3. в котором в качестве разделителя используется символ перевода строки ('\n', 0x0A),
//    например, 'auth_date=<auth_date>\nquery_id=<query_id>\nuser=<user>'.
//
//    Полная проверка может выглядеть так:
//
//    data_check_string = ...
//    secret_key = HMAC_SHA256(<bot_token>, "WebAppData")
//    if (hex(HMAC_SHA256(data_check_string, secret_key)) == hash) {
//        // data is from Telegram
//    }
//
// "WebAppData" как ключ, токен бота как сообщение

    @Override
    public boolean checkTelegramAuthorization(String initData) {
        Map<String, String> parsedData = parseInitData(initData);
        String receivedHash = parsedData.remove("hash");

        String dataCheckString = parsedData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())                     // 1.
                .map(entry -> entry.getKey() + "=" + entry.getValue())  // 2.
                .collect(Collectors.joining("\n"));             // 3.

        // Генерация секретного ключа
        byte[] secretKey = hmacSha256(telegramBot.getBotToken(), "WebAppData" .getBytes(StandardCharsets.UTF_8));

        // Подпись строки data_check_string с использованием секретного ключа
        byte[] hashBytes = hmacSha256(dataCheckString, secretKey);
        String calculatedHash = bytesToHex(hashBytes);

        boolean isHashEquals = calculatedHash.equals(receivedHash);

        long authDate = Long.parseLong(parsedData.get("auth_date"));
        long now = System.currentTimeMillis() / 1000;
        boolean isActualData = (now - authDate) < (24 * 60 * 60);

        return isHashEquals && isActualData;
    }

    private static byte[] hmacSha256(String data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            mac.init(keySpec);
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при вычислении HMAC", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    @Override
    public UserDto getUser(String initData) {
        try {
            Map<String, String> data = parseInitData(initData);
            String userJson = data.get("user");
            return objectMapper.readValue(userJson, UserDto.class);

        } catch (Exception e) {
            log.error("Can't parse user data: {}. {}", initData, e);
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        for (String pair : initData.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                result.put(parts[0], URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            }
        }
        return result;
    }
}
