package edu.praktikum.sprint7.utils;

import com.google.gson.Gson;
import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

public class OrderUtils {

    static {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Конвертация заказа в JSON")
    public static String toJson(Order order) {
        Gson gson = new Gson();
        return gson.toJson(order);
    }

    @Step("Создание заказа")
    public static Response createOrder(Order order) {
        return RestAssured.given()
                .contentType("application/json")
                .body(toJson(order))
                .when()
                .post("api/v1/orders")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    @Step("Получение списка заказов")
    public static Response getOrders() {
        return RestAssured.given()
                .when()
                .get("api/v1/orders")
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();
    }
    @Step("Проверка статус кода и тела ответа")
    public static void checkResponse(Response response, int expectedStatusCode) {
        SoftAssertions softAssertions = new SoftAssertions();


        int actualStatusCode = response.statusCode();
        softAssertions.assertThat(actualStatusCode).isEqualTo(expectedStatusCode);


        if (actualStatusCode == expectedStatusCode) {
            String responseBody = response.getBody().asString();
            softAssertions.assertThat(responseBody)
                    .as("Тело ответа не должно быть пустым")
                    .isNotNull()
                    .contains("track");
        }

        softAssertions.assertAll();
    }
    @Step("Проверка статус кода и наличия поля 'orders' в теле ответа")
    public static void checkOrdersField(Response response, int expectedStatusCode) {
        SoftAssertions softAssertions = new SoftAssertions();


        int actualStatusCode = response.statusCode();
        softAssertions.assertThat(actualStatusCode).isEqualTo(expectedStatusCode);

        if (actualStatusCode == expectedStatusCode) {
            String responseBody = response.getBody().asString();
            softAssertions.assertThat(responseBody).as("Тело ответа не должно быть пустым").isNotNull();
            softAssertions.assertThat(responseBody).contains("orders");
        }

        softAssertions.assertAll();
    }
    @Step("Логирование ответа")
    public static void logResponse(Response response) {
        String responseBody = response.getBody().asString();
        String responseHeaders = response.getHeaders().toString();
        int statusCode = response.statusCode();

        String message = String.format("Response Status Code: %d\nHeaders: %s\nBody: %s",
                statusCode, responseHeaders, responseBody);

        Allure.addAttachment("API Response", message);
    }
}