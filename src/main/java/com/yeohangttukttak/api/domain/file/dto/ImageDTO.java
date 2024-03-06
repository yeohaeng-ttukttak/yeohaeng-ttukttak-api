package com.yeohangttukttak.api.domain.file.dto;

import com.yeohangttukttak.api.domain.file.entity.File;
import lombok.Data;

@Data
public class ImageDTO {

    private Long id;

    private String url;

    private String mimeType;

    public ImageDTO (File file) {
        this.id = file.getId();
        this.url = file.getUrl();
        this.mimeType = file.getMimeType();
    }

}
