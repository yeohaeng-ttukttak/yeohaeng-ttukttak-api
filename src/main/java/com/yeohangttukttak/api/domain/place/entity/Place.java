package com.yeohangttukttak.api.domain.place.entity;

import com.yeohangttukttak.api.global.interfaces.Attachable;
import com.yeohangttukttak.api.domain.BaseEntity;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Place extends BaseEntity implements Attachable {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PlaceType type;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "place")
    private List<Visit> visits = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "place_id")
    private List<File> files = new ArrayList<>();

    @Builder
    public Place(Long id, String name, PlaceType type, Location location) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
    }

}
