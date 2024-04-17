// db-14: POST user API
package com.example.postgres.user.routes;

import com.example.postgres.base.routes.BaseRoutes;

public class UserRoutes {
    //private final static String ROOT = "/api/v1/user";
    // Block 09 - Spring Boot Security
    private final static String ROOT = BaseRoutes.API + "/user";
    public final static String CREATE = ROOT;

    // db-15:GET user by ID
    public final static String BY_ID = ROOT + "/{id}";

    // db-16:GET all users
    public final static String SEARCH = ROOT;

    // Block 09 - Spring Boot Security
    public final static String TEST = BaseRoutes.NOT_SECURED + "/test";

}
