package com.mtx.todolist.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
    RUNNING,
    COMPLETED,
    FAILED;

    public static Optional<Status> find(String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(status))
                .findFirst();
    }
}
