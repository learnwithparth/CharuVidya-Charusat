package com.codehat.charusat.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidUserException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidUserException() {
        super(ErrorConstants.INVALID_USER_TYPE, "Incorrect login", Status.BAD_REQUEST);
    }
}
