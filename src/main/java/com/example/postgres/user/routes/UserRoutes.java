// db-14: POST user API
package com.example.postgres.user.routes;

public class UserRoutes {
    private final static String ROOT = "/api/v1/user";
    public final static String CREATE = ROOT;

    // db-15:GET user by ID
    public final static String BY_ID = ROOT + "/{id}";

    // db-16:GET all users
    public final static String SEARCH = ROOT;
}
