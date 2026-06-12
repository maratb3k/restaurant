package com.restaurant.booking.model.constant;

import lombok.Getter;

@Getter
public enum SortBy {
    POPULARITY_ASC("popularity,asc"),
    POPULARITY_DESC("popularity,desc"),
    PRICE_ASC("price,asc"),
    PRICE_DESC("price,desc");

    private final String value;

    SortBy(String value) {
        this.value = value;
    }
}
