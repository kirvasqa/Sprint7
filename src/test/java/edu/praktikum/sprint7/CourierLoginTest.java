package edu.praktikum.sprint7;

import edu.praktikum.sprint7.utils.Utils;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CourierLoginTest {

    private String createdCourierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Логин курьера")
    public void courierCanLogin() {
        Map<String, String> credentials = Utils.createCourier();
        String login = credentials.get("login");
        String password = credentials.get("password");

        // Логинимся
        createdCourierId = Utils.loginCourier(login, password);
        assertNotNull("ID курьера должен быть не null", createdCourierId);
    }

    @Test
    @DisplayName("Логин курьера с неправильным паролем")
    public void loginWithWrongPassword() {
        Map<String, String> credentials = Utils.createCourier();
        String correctLogin = credentials.get("login");

        String incorrectPassword = "wrongPassword";
        Response response = Utils.loginCourierWithResponse(correctLogin, incorrectPassword);
        assertEquals("Неверный статус код для логина с неправильным паролем", 404, response.statusCode());
        assertTrue("Ответ не содержит сообщение об ошибке", response.body().asString().contains("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера с неправильным логином")
    public void loginWithWrongLogin() {
        Map<String, String> credentials = Utils.createCourier();
        String correctPassword = credentials.get("password");

        String incorrectLogin = "wrongLogin";
        Response responseWithWrongLogin = Utils.loginCourierWithResponse(incorrectLogin, correctPassword);
        assertEquals("Неверный статус код для логина с неправильным логином", 404, responseWithWrongLogin.statusCode());
        assertTrue("Ответ не содержит сообщение об ошибке", responseWithWrongLogin.body().asString().contains("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void loginWithoutLogin() {
        Response responseWithoutLogin = Utils.loginCourierWithResponse("", "anyPassword");
        assertEquals("Неверный статус код для логина без логина", 400, responseWithoutLogin.statusCode());
        assertTrue("Ответ не содержит сообщение об ошибке", responseWithoutLogin.body().asString().contains("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void loginWithoutPassword() {
        Map<String, String> credentials = Utils.createCourier();
        String login = credentials.get("login");

        Response responseWithoutPassword = Utils.loginCourierWithResponse(login, "");
        assertEquals("Неверный статус код для логина без пароля", 400, responseWithoutPassword.statusCode());
        assertTrue("Ответ не содержит сообщение об ошибке", responseWithoutPassword.body().asString().contains("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без логина и пароля")
    public void loginWithoutLoginAndPassword() {
        Response responseWithoutPasswordAndLogin = Utils.loginCourierWithResponse("", "");
        assertEquals("Неверный статус код для логина без пароля и логина", 400, responseWithoutPasswordAndLogin.statusCode());
        assertTrue("Ответ не содержит сообщение об ошибке", responseWithoutPasswordAndLogin.body().asString().contains("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин несущесутвующего курьера")
    public void loginNonExistentCourier() {
        String nonExistentLogin = "nonExistent";
        String nonExistentPassword = "nonExistentPassword";

        Response response = Utils.loginCourierWithResponse(nonExistentLogin, nonExistentPassword);
        assertEquals("Неверный статус код для входа несуществующего курьера", 404, response.statusCode());
        assertTrue("Ответ не содержит сообщение об ошибке", response.body().asString().contains("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        // Удаляем курьера после выполнения тестов
        if (createdCourierId != null) {
            Utils.deleteCourier(createdCourierId);
        }
    }
}