package com.mtx.todolist.validator;

public interface Validator<T> {
    ValidationResult validate(T object);
}
