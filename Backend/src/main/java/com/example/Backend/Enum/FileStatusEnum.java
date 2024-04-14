package com.example.Backend.Enum;

import lombok.Data;
import lombok.Getter;

@Getter
public enum FileStatusEnum {
    CREATE(0,"新增"),
    MODIFY(1,"修改");

    private final int code;
    private final String value;

    FileStatusEnum(int code,String value) {
        this.code = code;
        this.value = value;
    }

}
