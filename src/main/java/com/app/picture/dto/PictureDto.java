package com.app.picture.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

}
