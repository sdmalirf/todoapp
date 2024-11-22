package com.mtx.todolist.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    public static Optional<Priority> find(String priority) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(priority))
                .findFirst();
    }
}
