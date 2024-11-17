package edu.scrapper.dto;

public record DriveItem(
        String name,
        String title,
        Folder folder,
        String mimeType,
        User owner,
        DriveFolder driveFolder
) {}