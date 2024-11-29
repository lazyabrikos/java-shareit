package ru.practicum.shareit.errors;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private final String error;
    private String description;
    private Map<String, String> errors = new HashMap<>();

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public ErrorResponse(String error, Map<String, String> errors) {
        this.error = error;
        this.description = "Some fields are incorrect";
        this.errors = errors;
    }
}
