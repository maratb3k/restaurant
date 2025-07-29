package com.epam.edp.demo.model.constant;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    WAITER("ROLE_WAITER"),
    CUSTOMER("ROLE_CUSTOMER"),
    VISITOR("ROLE_VISITOR");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
