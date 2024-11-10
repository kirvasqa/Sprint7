package edu.praktikum.sprint7;

import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.utils.Utils;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import io.restassured.response.Response;

public class CourierTest {

    private String createdCourierId;

    @Test
    @DisplayName("Создание курьера")
    public void createCourier() {
        Courier courier = Utils.generateCourierData(true, true, true);

        Response response = Utils.createCourier(courier);
        try {
            Utils.checkStatusCodeAndResponse(response, 201, true, null);
            createdCourierId = Utils.loginCourier(courier);
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Создание дубля курьера")
    public void createDuplicateCourier() {
        Courier courier = Utils.generateCourierData(true, true, true);

        Response firstResponse = Utils.createCourier(courier);
        Response secondResponse = null;
        try {
            Utils.checkStatusCodeAndResponse(firstResponse, 201, true, null);
            secondResponse = Utils.createCourier(courier);
            Utils.checkStatusCodeAndResponse(secondResponse, 409, "Этот логин уже используется");
        } finally {
            Utils.logResponse(firstResponse);
            Utils.logResponse(secondResponse);
        }
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        Courier courier = Utils.generateCourierData(false, true, true);

        Response response = Utils.createCourier(courier);
        try {
            Utils.checkStatusCodeAndResponse(response, 400, "Недостаточно данных для создания учетной записи");
        } finally {
            Utils.logResponse(response);
        }
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        Courier courier = Utils.generateCourierData(true, false, true);

        Response response = Utils.createCourier(courier);
        try {
            Utils.checkStatusCodeAndResponse(response, 400, "Недостаточно данных для создания учетной записи");
        } finally {
            Utils.logResponse(response);
        }
    }

    @After
    public void tearDown() {
        if (createdCourierId != null) {
            Utils.deleteCourier(createdCourierId);
            createdCourierId = null;
        }
    }
}