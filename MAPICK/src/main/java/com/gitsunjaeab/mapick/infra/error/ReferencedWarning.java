package com.gitsunjaeab.mapick.infra.error;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReferencedWarning {

    private String key = null;
    private ArrayList<Object> params = new ArrayList<>();

    public void addParam(final Object param) {
        params.add(param);
    }
}
