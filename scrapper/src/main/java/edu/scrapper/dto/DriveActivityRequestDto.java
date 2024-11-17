package edu.scrapper.dto;

import jakarta.validation.constraints.NotBlank;

public record DriveActivityRequestDto(
        @NotBlank String itemName,
        int pageSize
) {
}
