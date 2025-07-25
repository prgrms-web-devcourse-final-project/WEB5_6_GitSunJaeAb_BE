package com.gitsunjaeab.mapick.api.roadmap.websocket.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LayerSocketAction {
    ADD,
    UPDATE,
    DELETE;

    @JsonCreator
    public static LayerSocketAction from(String value) {
        return LayerSocketAction.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase(); // JSON 응답 시엔 소문자로 출력
    }
}
