package com.yeohangttukttak.api.domain.visit.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.GeometryExpression;
import com.querydsl.spatial.GeometryExpressions;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.travel.entity.*;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.Geometry;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.querydsl.spatial.GeometryExpressions.asGeometry;
import static com.yeohangttukttak.api.domain.place.entity.QPlace.place;
import static com.yeohangttukttak.api.domain.travel.entity.QTravel.travel;
import static com.yeohangttukttak.api.domain.visit.entity.QVisit.visit;

@Repository
@Slf4j
public class VisitRepository {

    private final JPAQueryFactory queryFactory;

    public VisitRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<VisitSearchResult> search(VisitSearch search) {

        NumberExpression<Double> distanceExpr = asGeometry(place.location.point)
                .distance(asGeometry(search.getLocation().getPoint())
        );

        List<Tuple> result = queryFactory
                .select(visit, travel, distanceExpr)
                .from(visit)
                .join(visit.travel, travel).fetchJoin()
                .join(visit.place, place).fetchJoin()
                .where(dwithin(search.getLocation(), search.getRadius()),
                        motivationsIn(search.getMotivations()),
                        accompanyTypesIn(search.getAccompanyTypes()),
                        ageGroupsIn(search.getAgeGroups()),
                        transportTypesIn(search.getTransportTypes())
                ).fetch();

        return result.stream().map(VisitSearchResult::new)
                .filter(record -> seasonsIn(search, record.getTravel()))
                .toList();
    }

    private BooleanTemplate dwithin(Location location, int radius) {
        return Expressions.booleanTemplate(
                "st_dwithin({0}, {1}, {2}, false) is true",
                place.location.point,
                location.getPoint(),
                radius
        );
    }

    private BooleanExpression motivationsIn(Set<Motivation> motivations) {
        return motivations != null ? travel.motivation.in(motivations) : null;
    }

    private BooleanExpression accompanyTypesIn(Set<AccompanyType> accompanyTypes) {
        return accompanyTypes != null ? travel.accompanyType.in(accompanyTypes) : null;
    }

    private BooleanExpression ageGroupsIn(Set<AgeGroup> ageGroups) {
        return ageGroups != null ? travel.member.ageGroup.in(ageGroups) : null;
    }

    private BooleanExpression transportTypesIn(Set<TransportType> transportTypes) {
        return transportTypes != null ? travel.transportType.in(transportTypes) : null;
    }

    private boolean seasonsIn(VisitSearch search, Travel travel) {
        if (search.getSeasons() == null) return true;

        Set<Season> intersection = travel.getPeriod().getSeasons();
        intersection.retainAll(search.getSeasons());
        return !intersection.isEmpty();
    }
}
