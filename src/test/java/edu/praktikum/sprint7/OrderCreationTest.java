package edu.praktikum.sprint7;

import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phoneNumber;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] inputColors;

    // Конструктор для параметризации
    public OrderCreationTest(String firstName, String lastName, String address, String metroStation,
                     String phoneNumber, int rentTime, String deliveryDate, String comment,
                     String[] inputColors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phoneNumber = phoneNumber;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.inputColors = inputColors;
    }

    @Parameterized.Parameters // Параметры для тестов
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Иван", "Иванов", "Москва, ул. Ленина, д. 1", "Киевская", "+79001234567", 5, "2023-10-01", "Комментарий", new String[]{"BLACK"}},
                {"Петр", "Петров", "Москва, ул. Пушкина, д. 2", "Арбатская", "+79001234568", 3, "2023-10-02", "Комментарий", new String[]{"GREY"}},
                {"Сидор", "Сидоров", "Москва, ул. Чехова, д. 3", "Краснопресненская", "+79001234569", 7, "2023-10-03", "Комментарий", new String[]{"BLACK", "GREY"}},
                {"Александр", "Александров", "Москва, ул. Академика, д. 4", "Щелковская", "+79001234570", 1, "2023-10-04", "Комментарий", null}
        });
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Test
    @DisplayName("Создание заказа")
    public void testCreateOrder() {
        Order order = new Order(firstName, lastName, address, metroStation,
                phoneNumber, rentTime, deliveryDate, comment, inputColors);


        Response response = given()
                .contentType("application/json")
                .body(order.toJson())
                .when()
                .post("api/v1/orders")
                .then()
                .statusCode(201) // Ожидаем, что статус ответа будет 201 Created
                .extract()
                .response();

        // Проверяем, что ответ содержит поле track
        String responseBody = response.getBody().asString();
        assertNotNull("Тело ответа не должно быть пустым", responseBody);
        assertThat(responseBody, containsString("track"));
    }
}