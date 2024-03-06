package com.yeohangttukttak.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
public class PageResult<T> {

    List<T> entities;

    PageInfo pageInfo;


    @Data
    @AllArgsConstructor
    public static class PageInfo {

        private Long page;

        private Long pageSize;

        private boolean hasNextPage;

    }


    public PageResult(List<T> entities, PageSearch search) {
        this.entities = entities;

        boolean hasNextPage = entities.size() > search.getPageSize();

        if (hasNextPage)
            this.entities.remove(this.entities.size() - 1);

        pageInfo = new PageInfo(
                search.getPage(), search.getPageSize(), hasNextPage);

    }

    private PageResult(List<T> entities, PageInfo pageInfo) {
        this.entities = entities;
        this.pageInfo = pageInfo;
    }

    public <E> PageResult<E> convertEntities(Function<T, E> converter) {

        List<E> convertEntities = entities.stream()
                .map(converter)
                .toList();

        return new PageResult<E>(convertEntities, pageInfo);

    }

}