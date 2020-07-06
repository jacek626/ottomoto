package com.app.utils.site.element;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class UploadedPicture {
    private final String uploadedFileName;
    private final String miniatureFileName;

    private UploadedPicture(String uploadedFile, String miniatureFile) {
        this.uploadedFileName = uploadedFile;
        this.miniatureFileName = miniatureFile;
    }

    public static UploadedPicture of(@NotNull String uploadedFile, @NotNull String miniatureFile) {
        return new UploadedPicture(uploadedFile, miniatureFile);
    }
}
