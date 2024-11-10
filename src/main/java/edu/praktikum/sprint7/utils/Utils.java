package edu.praktikum.sprint7.utils;

import com.google.gson.Gson;
import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.generators.CourierDataGenerator;
import edu.praktikum.sprint7.models.JsonResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;


import static org.assertj.core.api.Assertions.assertThat;

public class Utils {

    static Gson gson = new Gson();

    @Step("Генерация данных курьера")
    public static Courier generateCourierData(boolean includeLogin, boolean includePassword, boolean includeFirstName) {
        String login = includeLogin ? CourierDataGenerator.generateRandomLogin(8) : null;
        String password = includePassword ? CourierDataGenerator.generateRandomPassword(8) : null;
        String firstName = includeFirstName ? CourierDataGenerator.generateRandomFirstName(9) : null;

        return new Courier(login, password, firstName);
    }

    @Step("Создание курьера")
    public static Response createCourier(Courier courier) {
        String courierJson = gson.toJson(courier);
        return createCourierOnServer(courierJson);
    }

    @Step("Создание курьера на сервере")
    public static Response createCourierOnServer(String courierJson) {
        return RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body(courierJson)
                .when()
                .post("api/v1/courier");
    }
    @Step("Проверка статус кода и тела ответа")
    public static void checkStatusCodeAndResponse(Response response, int expectedStatusCode, boolean checkOk, String expectedMessage) {
        int actualStatusCode = response.statusCode();


        assertThat(actualStatusCode)
                .as("Статус-код ответа не соответствует ожидаемому: ожидался " + expectedStatusCode + ", получен " + actualStatusCode)
                .isEqualTo(expectedStatusCode);

        String responseBody = response.getBody().asString();
        parseResponse(responseBody);
        JsonResponse jsonResponse = gson.fromJson(responseBody, JsonResponse.class);

        // Выполнение проверок на основе статус-кода
        switch (actualStatusCode) {
            case 200:
                validateResponseFor200(jsonResponse);
                break;
            case 201:
                validateResponseFor201(jsonResponse, checkOk);
                break;
            default:
                validateErrorResponse(jsonResponse, expectedMessage);
                break;
        }
    }

    @Step("Проверка статус-кода и тела ответа при создании курьера")
    public static void checkStatusCodeAndResponse(Response response, int expectedStatusCode, String expectedMessage) {
        boolean checkOk = (expectedStatusCode == 201);


        checkStatusCodeAndResponse(response, expectedStatusCode, checkOk, expectedMessage);
    }

    private static void validateResponseFor200(JsonResponse jsonResponse) {
        assert jsonResponse.getId() != null : "Ответ должен содержать поле 'id'";
    }

    private static void validateResponseFor201(JsonResponse jsonResponse, boolean checkOk) {
        if (checkOk) {
            assert jsonResponse.isOk() : "Ответ должен содержать поле 'ok' со значением true";
        }
    }

    private static void validateErrorResponse(JsonResponse jsonResponse, String expectedMessage) {
        String actualMessage = jsonResponse.getMessage();
        assert actualMessage.equals(expectedMessage) : "Сообщение об ошибке должно совпадать с ожидаемым";
    }



    @Step("Парсинг ответа")
    private static void parseResponse(String responseBody) {

        JsonResponse jsonResponse = gson.fromJson(responseBody, JsonResponse.class);


        if (jsonResponse == null) {
            throw new IllegalArgumentException("Неверный формат ответа. Ожидался JSON, получен: " + responseBody);
        }
    }



    @Step("Логин курьера для получения id под удаление")
    public static String loginCourier(Courier courier) {
        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("api/v1/courier/login");

        return response.jsonPath().getString("id");  // Возвращаем id курьера
    }

    @Step("Удаление курьера с ID: {0}")
    public static void deleteCourier(String id) {
        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .when()
                .delete("api/v1/courier/" + id);

       if (response.statusCode() != 200) {
            throw new RuntimeException("Не удалось удалить курьера: " + response.body().asString());
        }
    }

    @Step("Логин курьера с данными")
    public static Response loginCourierWithResponse(Courier courier) {
        String courierJson = gson.toJson(courier);

        Response response = RestAssured.given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .header("Content-type", "application/json")
                .body(courierJson)
                .when()
                .post("api/v1/courier/login");

        return response;
    }

    @Step("Логирование ответа")
    public static void logResponse(Response response) {
        if (response != null) {
            String responseBody = response.getBody().asString();
            String responseHeaders = response.getHeaders().toString();
            int statusCode = response.statusCode();


            String formattedResponseBody = responseBody != null && !responseBody.isEmpty() ? responseBody : "Тело ответа пустое";
            String formattedResponseHeaders = responseHeaders != null && !responseHeaders.isEmpty() ? responseHeaders : "Заголовки ответа пустые";

            String message = String.format("Response Status Code: %d\nHeaders: %s\nBody: %s",
                    statusCode, formattedResponseHeaders, formattedResponseBody);

            Allure.addAttachment("API Response", message);
        } else {

            Allure.addAttachment("API Response", "Ответ сервера равен null");
        }
    }
}