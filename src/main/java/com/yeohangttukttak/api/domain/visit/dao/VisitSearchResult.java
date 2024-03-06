package com.yeohangttukttak.api.domain.visit.dao;

import com.querydsl.core.Tuple;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitSearchResult {

    private Visit visit;

    private Travel travel;

    private Double distance;

    public VisitSearchResult(Tuple tuple) {
        visit = tuple.get(0, Visit.class);
        travel = tuple.get(1, Travel.class);
        distance = tuple.get(2, Double.class);
    }

}
