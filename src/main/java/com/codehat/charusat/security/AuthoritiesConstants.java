package com.codehat.charusat.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Added the 2 profiles i.e., STUDENT and FACULTY
     * */
    public static final String STUDENT = "ROLE_STUDENT";

    public static final String FACULTY = "ROLE_FACULTY";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
