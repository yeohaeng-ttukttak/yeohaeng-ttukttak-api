package com.yeohangttukttak.api.domain.place.dto;

import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.global.config.validator.ValidLatitude;
import com.yeohangttukttak.api.global.config.validator.ValidLongitude;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class LocationDTO {

    @ValidLatitude
    private Double latitude;

    @ValidLongitude
    private Double longitude;

    private Double distance;

    public LocationDTO (Location location) {
        this.latitude = location.getLatitude() ;
        this.longitude = location.getLongitude();
    }

    public LocationDTO (Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setDistance(Double distance) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.distance = Double.parseDouble(df.format(distance * 100));
    }

}
