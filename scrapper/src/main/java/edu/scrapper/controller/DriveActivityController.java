package edu.scrapper.controller;

import edu.scrapper.dto.DriveActivityRequestDto;
import edu.scrapper.dto.DriveActivityResponseDto;
import edu.scrapper.service.DriveActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity")
public class DriveActivityController {

    private static final Logger logger = LoggerFactory.getLogger(DriveActivityController.class);
    private final DriveActivityService driveActivityService;

    @PostMapping("/query")
    public ResponseEntity<DriveActivityResponseDto> queryActivity(
            @Valid @RequestBody DriveActivityRequestDto requestDto,
            Authentication authentication) {

        logger.info("Received query request: {}", requestDto);

        try {
            DriveActivityResponseDto response = driveActivityService.getActivity(requestDto, authentication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while processing request: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
