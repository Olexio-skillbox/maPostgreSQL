// db-14: POST user API
package com.example.postgres.user.routes;

import com.example.postgres.base.routes.BaseRoutes;

public class UserRoutes {
    //private final static String ROOT = "/api/v1/user";
    // Block 09 - Spring Boot Security
    private final static String ROOT = BaseRoutes.API + "/user";

    // Block 09.2 Spring Security
    //public final static String CREATE = ROOT;
    public final static String REGISTRATION = BaseRoutes.NOT_SECURED + "/registration";
    public final static String EDIT = ROOT;

    // db-15:GET user by ID
    public final static String BY_ID = ROOT + "/{id}";

    // db-16:GET all users
    public final static String SEARCH = ROOT;

    // Block 09 - Spring Boot Security
    // public final static String TEST = BaseRoutes.NOT_SECURED + "/test";
    // Block 09.2 - Spring Security
    public final static String INIT = BaseRoutes.NOT_SECURED + "/init";

}
