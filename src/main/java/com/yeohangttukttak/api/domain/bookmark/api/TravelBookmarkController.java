package com.yeohangttukttak.api.domain.bookmark.api;

import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.service.*;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class TravelBookmarkController {

    private final TravelBookmarkCreateService bookmarkCreateService;
    private final TravelBookmarkDeleteService bookmarkDeleteService;
    private final TravelBookmarkFindService bookmarkFindService;

    @PostMapping("/travels/{id}")
    public ResponseEntity<ApiResponse<BookmarkDTO>> createPlaceBookmark(
            @PathVariable("id") Long id, HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        BookmarkDTO dto = bookmarkCreateService.call(accessToken.getEmail(), id);

        return ResponseEntity.created(URI.create("/api/v1/places/" + dto.getTargetId()))
                .body(new ApiResponse<>(dto));
    }

    @DeleteMapping("/travels/{id}")
    public ApiResponse<BookmarkDTO> deletePlaceBookmark(
            @PathVariable("id") Long id, HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        BookmarkDTO dto = bookmarkDeleteService.call(accessToken.getEmail(), id);

        return new ApiResponse<>(dto);
    }

    @GetMapping("/travels")
    public ApiResponse<List<BookmarkDTO>> findPlaceBookmarks(HttpServletRequest request) {
        JwtToken accessToken = (JwtToken) request.getAttribute("accessToken");
        List<BookmarkDTO> dtos = bookmarkFindService.call(accessToken.getEmail());

        return new ApiResponse<>(dtos);
    }

}
