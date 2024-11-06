package edu.praktikum.sprint7.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Courier {
    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    @JsonProperty("firstName")
    private String firstName;

    public Courier withLogin(String login) {
        this.login = login;
        return this;
    }

    public Courier withPassword(String password) {
        this.password = password;
        return this;
    }

    public Courier withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }
}