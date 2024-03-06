package com.yeohangttukttak.api.domain.place.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Point;
import org.geolatte.geom.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    private Double latitude;

    private Double longitude;

    private Point point;

    // 클래스 레벨의 정적 변수로 선언해 한 번만 초기화 한다.
    private static final GeometryFactory factory = new GeometryFactory(
            new PrecisionModel(), 4326);

    public Location (Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.point = JTS.from(factory.createPoint(new Coordinate(longitude, latitude)));
    }

    public Location (Point point) {
        this.point = point;
        this.longitude = point.getPosition().getCoordinate(0);
        this.latitude = point.getPosition().getCoordinate(1);
    }

}
