package com.yeohangttukttak.api.domain.place.api;

import com.yeohangttukttak.api.domain.file.dto.ImageDTO;
import com.yeohangttukttak.api.domain.file.entity.File;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.global.common.ApiResponse;
import com.yeohangttukttak.api.global.common.PageResult;
import com.yeohangttukttak.api.global.common.PageSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceRepository placeRepository;

    @GetMapping("/{id}/images")
    public ApiResponse<PlaceImageDTO> getPlaceImages(
            @PathVariable("id") Long id,
            @ModelAttribute PageSearch search) {

        PageResult<File> images = placeRepository.getPlaceImage(id, search);

        return new ApiResponse<>(new PlaceImageDTO(images.convertEntities(ImageDTO::new)));

    }

    @Data
    @AllArgsConstructor
    public static class PlaceImageDTO {

        PageResult<ImageDTO> images;

    }


}
