package edu.praktikum.sprint7.generators;

import java.util.Random;

public class CourierDataGenerator {

    private static final String LATIN_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CYRILLIC_CHARACTERS = "абвгдейтёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕИТЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final Random RANDOM = new Random();

    public static String generateRandomString(String characters, int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return result.toString();
    }

    public static String generateRandomLogin(int length) {
        return generateRandomString(LATIN_CHARACTERS, length);
    }

    public static String generateRandomPassword(int length) {
        return generateRandomString(LATIN_CHARACTERS, length);
    }

    public static String generateRandomFirstName(int length) {
        return generateRandomString(CYRILLIC_CHARACTERS, length);
    }
}