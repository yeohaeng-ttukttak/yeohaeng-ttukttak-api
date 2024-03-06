package com.yeohangttukttak.api.domain.place.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yeohangttukttak.api.domain.file.entity.QFile.file;
import static com.yeohangttukttak.api.domain.place.entity.QPlace.place;

@Repository
public class PlaceRepository {

    private final JPAQueryFactory queryFactory;

    public PlaceRepository (EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public PageResult<File> getPlaceImage(Long id, PageSearch search) {

        List<File> files = queryFactory.select(file)
                .from(place)
                .join(place.files, file)
                .where(place.id.eq(id))
                .offset(search.getOffset())
                .limit(search.getPageSize() + 1)
                .orderBy(file.id.asc())
                .fetch();

        return new PageResult<>(files, search);
    }



}
