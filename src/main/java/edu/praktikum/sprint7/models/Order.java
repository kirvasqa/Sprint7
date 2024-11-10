package edu.praktikum.sprint7.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phoneNumber;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] colors;
}