package edu.praktikum.sprint7;

import edu.praktikum.sprint7.utils.OrderUtils;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

public class OrderListTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrders() {
        Response response = OrderUtils.getOrders();

        try {
            OrderUtils.checkOrdersField(response, 200);
        } finally {
            OrderUtils.logResponse(response);
        }
    }
}