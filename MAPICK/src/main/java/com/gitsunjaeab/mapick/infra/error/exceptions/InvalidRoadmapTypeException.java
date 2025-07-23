package com.gitsunjaeab.mapick.infra.error.exceptions;

public class InvalidRoadmapTypeException extends RuntimeException {
    public InvalidRoadmapTypeException(String message) {
        super(message);
    }
}