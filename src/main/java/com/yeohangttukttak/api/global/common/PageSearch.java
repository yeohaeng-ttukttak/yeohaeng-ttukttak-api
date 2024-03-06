package com.yeohangttukttak.api.global.common;

import lombok.Data;

@Data
public class PageSearch {

    private Long page;

    private Long pageSize;

    public Long getOffset() {
        return (page - 1) * pageSize;
    }

}