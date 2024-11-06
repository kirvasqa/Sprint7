package edu.praktikum.sprint7;

import edu.praktikum.sprint7.generators.CourierDataGenerator;
import edu.praktikum.sprint7.utils.Utils;
import edu.praktikum.sprint7.models.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class CourierTest {

    private String createdCourierId;

    @Test
    @DisplayName("Создание курьера")
    public void createCourier() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        // Создаем курьера с автоматически сгенерированными данными
        Map<String, String> credentials = Utils.createCourier();
        String login = credentials.get("login");
        String password = credentials.get("password");

        // Логинимся, получаем ID созданного курьера
        createdCourierId = Utils.loginCourier(login, password);
    }

    @Test
    @DisplayName("Создание дубля курьера")
    public void createDuplicateCourier() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        // Создаем первого курьера
        Map<String, String> credentials = Utils.createCourier();
        String login = credentials.get("login");
        String password = credentials.get("password");

        // Логинимся и получаем ID курьера
        createdCourierId = Utils.loginCourier(login, password);

        // Попытка создания второго курьера с теми же данными
        Courier duplicateCourier = new Courier()
                .withLogin(login)
                .withPassword(password)
                .withFirstName("TestDuplicate");

        Response response = given()
                .header("Content-type", "application/json")
                .body(duplicateCourier)
                .when()
                .post("api/v1/courier");

        // Проверка на дублирование
        assertEquals("Неверный статус код для дубля", 409, response.statusCode());

        // Логинимся с полученными данными
        createdCourierId = Utils.loginCourier(login, password);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        // Генерация данных для запроса
        String password = CourierDataGenerator.generateRandomPassword(10);
        String firstName = CourierDataGenerator.generateRandomFirstName(5);

        // Создание курьера без логина
        Courier courier = new Courier()
                .withPassword(password)
                .withFirstName(firstName);

        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("api/v1/courier");

        assertEquals("Неверный статус код", 400, response.statusCode());
        assertTrue("Ответ не содержит ошибки", response.jsonPath().getString("message").contains("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        // Генерация данных для запроса
        String login = CourierDataGenerator.generateRandomLogin(8);
        String firstName = CourierDataGenerator.generateRandomFirstName(5);

        // Создание курьера без пароля
        Courier courier = new Courier()
                .withLogin(login)
                .withFirstName(firstName);

        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("api/v1/courier");

        assertEquals("Неверный статус код", 400, response.statusCode());
        assertTrue("Ответ не содержит ошибки", response.jsonPath().getString("message").contains("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        // Удаляем созданного курьера после выполнения каждого теста
        if (createdCourierId != null) {
            Utils.deleteCourier(createdCourierId);
        }
    }
}