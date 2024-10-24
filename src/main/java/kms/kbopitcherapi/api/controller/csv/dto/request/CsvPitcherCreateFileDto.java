package kms.kbopitcherapi.api.controller.csv.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CsvPitcherCreateFileDto(
        @NotEmpty String filePath,
        @NotEmpty String fileName
) {

    public String getFullFilePath() {
        if (filePath.endsWith("/"))
            return filePath + fileName;

        return filePath + "/" + fileName;
    }
}
