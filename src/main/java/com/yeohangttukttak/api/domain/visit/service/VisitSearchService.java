package com.yeohangttukttak.api.domain.visit.service;

import com.querydsl.core.Tuple;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.travel.dto.TravelDTO;
import com.yeohangttukttak.api.domain.travel.entity.Season;
import com.yeohangttukttak.api.domain.visit.dao.VisitSearchResult;
import com.yeohangttukttak.api.domain.visit.entity.Visit;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearch;
import com.yeohangttukttak.api.domain.visit.dto.VisitSearchDTO;
import com.yeohangttukttak.api.domain.visit.dao.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VisitSearchService {

    private final VisitRepository visitRepository;

    public VisitSearchDTO search(VisitSearch search) {
        List<VisitSearchResult> result = visitRepository.search(search);

        List<Visit> visits = result.stream().map(VisitSearchResult::getVisit).toList();
        List<Double> distances = result.stream().map(VisitSearchResult::getDistance).toList();

        // place 탐색
        List<PlaceDTO> placeDTOS = visits.stream()
                .collect(groupingBy(Visit::getPlace,
                        mapping(Visit::getTravel, toList())
                ))
                .entrySet().stream()
                .map(PlaceDTO::new)
                .toList();

        AtomicInteger index = new AtomicInteger();

        placeDTOS = placeDTOS.stream()
                .peek((placeDTO) ->
                        placeDTO.getLocation()
                        .setDistance(distances.get(index.getAndIncrement())))
                .sorted(comparingDouble(PlaceDTO::getId))
                .toList();

        // travelDTO 조립
        List<TravelDTO> travelDTOS = visits.stream()
                .map(Visit::getTravel)
                .distinct()
                .map(TravelDTO::new)
                .sorted(comparingDouble(TravelDTO::getId))
                .toList();

        return new VisitSearchDTO(travelDTOS, placeDTOS);
    }




}
