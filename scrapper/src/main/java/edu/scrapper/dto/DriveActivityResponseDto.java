package edu.scrapper.dto;


import java.util.List;

public record DriveActivityResponseDto(
        List<DriveActivity> activities
) {
}

