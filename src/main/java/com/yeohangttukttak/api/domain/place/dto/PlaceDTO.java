package com.yeohangttukttak.api.domain.place.dto;
import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.entity.PlaceType;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;


@Data
public class PlaceDTO {

    private Long id;

    private String name;

    private LocationDTO location;

    private PlaceType type;

    private List<ImageDTO> images;

    private List<Reference> travels;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.type = place.getType();
        this.location = new LocationDTO(place.getLocation());
        this.images = place.getFiles().stream().map(ImageDTO::new).toList();
    }


    public PlaceDTO(Map.Entry<Place, List<Travel>> entry) {
        this(entry.getKey());

        this.travels = entry.getValue().stream()
                .map(travel -> new Reference(travel.getId(), "travel"))
                .distinct().toList();
    }

    @Data
    @AllArgsConstructor
    public static class Reference {

        private Long id;

        private String type;

    }

}
