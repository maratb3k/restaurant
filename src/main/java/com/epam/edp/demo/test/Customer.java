package com.epam.edp.demo.test;

import jakarta.persistence.*;

@Entity
@Table
public class Customer {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
