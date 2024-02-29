package com.yeohangttukttak.api.service.visit;

import com.yeohangttukttak.api.domain.member.AgeGroup;
import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.travel.AccompanyType;
import com.yeohangttukttak.api.domain.travel.Motivation;
import com.yeohangttukttak.api.domain.travel.TransportType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
public class VisitSearch {

    private Location location;

    private int radius;

    private Motivation motivation;

    private AccompanyType accompanyType;

    private AgeGroup ageGroup;

    private TransportType transportType;

    @Builder
    public VisitSearch(Location location,
                       int radius,
                       Motivation motivation,
                       AccompanyType accompanyType,
                       AgeGroup ageGroup,
                       TransportType transportType) {
        this.location = location;
        this.radius = radius;
        this.motivation = motivation;
        this.accompanyType = accompanyType;
        this.ageGroup = ageGroup;
        this.transportType = transportType;
    }
}
