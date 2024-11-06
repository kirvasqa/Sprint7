package edu.praktikum.sprint7.models;

import com.google.gson.Gson;

public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phoneNumber;
    private int rentTime; // Время аренды
    private String deliveryDate; // Дата доставки
    private String comment; // Комментарий
    private String[] colors; // Цвета

    public Order(String firstName, String lastName, String address, String metroStation,
                 String phoneNumber, int rentTime, String deliveryDate, String comment, String[] colors) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phoneNumber = phoneNumber;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colors = colors;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}