package com.mtx.todolist.exception;

import com.mtx.todolist.validator.Error;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    @Getter
    private final List<Error> errors;
    public ValidationException(List<Error> errors) {
        this.errors = errors;
    }
}
