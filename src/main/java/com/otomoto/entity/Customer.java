package com.otomoto.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer extends Person {

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
