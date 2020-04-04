package com.app.utils;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.File;

@Getter
@Builder
public class UploadedPicture {
    private final File uploadedFile;
    private final File miniatureFile;
    private final String originalFilename;

    private UploadedPicture(File uploadedFile, File miniatureFile, String originalFilename) {
        this.uploadedFile = uploadedFile;
        this.miniatureFile = miniatureFile;
        this.originalFilename = originalFilename;
    }

    public static UploadedPicture of(@NotNull File uploadedFile, @NotNull File miniatureFile, @NotNull String originalFilename) {
        return new UploadedPicture( uploadedFile, miniatureFile, originalFilename);
    }
}
