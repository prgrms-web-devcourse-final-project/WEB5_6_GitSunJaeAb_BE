package com.gitsunjaeab.mapick.api.member.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SimpleMessageResponse {

    private String message;

    public SimpleMessageResponse(String message) {
        this.message = message;
    }

    public static SimpleMessageResponse of(String message) {
        return new SimpleMessageResponse(message);
    }
} 