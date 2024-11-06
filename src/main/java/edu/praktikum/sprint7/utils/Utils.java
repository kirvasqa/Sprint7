package edu.praktikum.sprint7.utils;

import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.generators.CourierDataGenerator;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Courier randomCourier() {
        return new Courier()
                .withLogin(CourierDataGenerator.generateRandomLogin(8))
                .withPassword(CourierDataGenerator.generateRandomPassword(8))
                .withFirstName(CourierDataGenerator.generateRandomFirstName(9));
    }

    public static Map<String, String> createCourier() {
        String login = CourierDataGenerator.generateRandomLogin(8);
        String password = CourierDataGenerator.generateRandomPassword(8);
        String firstName = CourierDataGenerator.generateRandomFirstName(9);


        Courier courier = new Courier()
                .withLogin(login)
                .withPassword(password)
                .withFirstName(firstName);


        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("api/v1/courier");

        // Проверка на успешное создание. Для того чтобы понять где сыпется было проще
        if (response.statusCode() != 201) {
            throw new RuntimeException("Не удалось создать курьера: " + response.body().asString());
        }

        // Возврат кредов
        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);
        return credentials;
    }
    public static String loginCourier(String login, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("api/v1/courier/login");

        if (response.statusCode() != 200) {
            throw new RuntimeException("Не удалось войти в систему: " + response.body().asString());
        }

        return response.jsonPath().getString("id");  // Возвращаем id курьера
    }
    public static void deleteCourier(String id) {
        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .when()
                .delete("api/v1/courier/" + id);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Не удалось удалить курьера: " + response.body().asString());
        }
    }
    public static Response loginCourierWithResponse(String login, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);

        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post("api/v1/courier/login");

        return response;
    }
}