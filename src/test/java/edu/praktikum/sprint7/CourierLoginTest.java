package edu.praktikum.sprint7;

import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.utils.Utils;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest {

    private Courier courier;
    private String createdCourierId;

    @Before
    public void setUp() {
        // Генерация и создание одного курьера перед тестами
        courier = Utils.generateCourierData(true, true, true);
        Response response = Utils.createCourier(courier);

        // Проверка на успешность создания курьера
        if (response.statusCode() != 201) {
            throw new IllegalStateException("Не удалось создать курьера: " + response.getBody().asString());
        }
        createdCourierId = Utils.loginCourier(courier);
    }

    @Test
    @DisplayName("Логин курьера")
    public void courierCanLogin() {
        Response response = Utils.loginCourierWithResponse(courier);
        try {
            Utils.checkStatusCodeAndResponse(response, 200, null);
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Логин курьера с неправильным паролем")
    public void loginWithWrongPassword() {
        Courier courierWithWrongPassword = new Courier(courier.getLogin(), "invalid_password", courier.getFirstName());
        Response response = Utils.loginCourierWithResponse(courierWithWrongPassword);
        try {
            Utils.checkStatusCodeAndResponse(response, 404, "Учетная запись не найдена"); // Проверяем статус код 404
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Логин курьера с неправильным логином")
    public void loginWithWrongLogin() {
        Courier courierWithWrongLogin = new Courier("invalidLogin", courier.getPassword(), courier.getFirstName());
        Response response = Utils.loginCourierWithResponse(courierWithWrongLogin);
        try {
            Utils.checkStatusCodeAndResponse(response, 404, "Учетная запись не найдена"); // Проверяем статус код 404
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void loginWithoutLogin() {
        Courier courierWithoutLogin = new Courier(null, courier.getPassword(), courier.getFirstName());
        Response response = Utils.loginCourierWithResponse(courierWithoutLogin);
        try {
            Utils.checkStatusCodeAndResponse(response, 400, "Недостаточно данных для входа"); // Проверяем статус код 400
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void loginWithoutPassword() {
        Courier courierWithoutPassword = new Courier(courier.getLogin(), null, courier.getFirstName());
        Response response = Utils.loginCourierWithResponse(courierWithoutPassword);
        try {
            Utils.checkStatusCodeAndResponse(response, 400, "Недостаточно данных для входа");
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Логин курьера без логина и пароля")
    public void loginWithoutLoginAndPassword() {
        Courier courierWithoutCredentials = new Courier(null, null, null);
        Response response = Utils.loginCourierWithResponse(courierWithoutCredentials);
        try {
            Utils.checkStatusCodeAndResponse(response, 400, "Недостаточно данных для входа");
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    public void loginNonExistentCourier() {
        Courier nonExistentCourier = new Courier("non_existent_login", "non_existent_password", null);
        Response response = Utils.loginCourierWithResponse(nonExistentCourier);
        try {
            Utils.checkStatusCodeAndResponse(response, 404, "Учетная запись не найдена");
        } finally {
            Utils.logResponse(response);
        }
    }

    @After
    public void tearDown() {

        if (createdCourierId != null) {
            Utils.deleteCourier(createdCourierId);
        }
    }
}