package com.yeohangttukttak.api.domain.travel.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccompanyType implements ValueBasedEnum {

    SOLO("solo"),       // 나홀로
    FRIENDS("friends"),    // 지인과
    PARENTS("parents"),    // 부모님과
    CHILDREN("children"),   // 자녀와
    FAMILY("family"),
    OTHERS("others");      // 기타

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

}
