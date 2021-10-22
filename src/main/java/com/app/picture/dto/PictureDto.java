package com.app.picture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class PictureDto {
    private Long id;

    @NotBlank
    private String repositoryName;

    @NotBlank
    private String miniatureRepositoryName;

    private String fileName;
    private boolean mainPhotoInAnnouncement;

    @Builder.Default
    private boolean pictureToDelete = false;

    @SuppressWarnings("unused")
    public PictureDto() {
    }

}
