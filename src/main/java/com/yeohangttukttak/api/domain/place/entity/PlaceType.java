package com.yeohangttukttak.api.domain.place.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yeohangttukttak.api.global.interfaces.ValueBasedEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PlaceType implements ValueBasedEnum {

    NATURE("nature"),     // 자연
    ATTRACTION("attraction"), // 관광지
    CULTURE("culture"),    // 문화시설
    LEISURE("leisure"),    // 레저/스포츠
    THEME("theme"), // 테마파크
    FOOD("food"),       // 식당/카페
    HOTEL("hotel");       // 숙소

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }


}
