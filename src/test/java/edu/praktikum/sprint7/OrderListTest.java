package edu.praktikum.sprint7;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;

public class OrderListTest {

    static {
        // Устанавливаем базовый URI для RestAssured
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получние списка заказов")
    public void testGetOrders() {
        // Выполняем GET-запрос для получения списка заказов
        Response response = RestAssured.given()
                .when()
                .get("api/v1/orders")
                .then()
                .statusCode(200) // Проверяем, что статус код 200
                .extract()
                .response();

        // Проверяем, что тело ответа не пустое и содержит поле orders
        assertNotNull("Тело ответа не должно быть пустым", response);
        assertThat("Ответ должен содержать список заказов", response.getBody().asString(), containsString("orders"));
    }
}