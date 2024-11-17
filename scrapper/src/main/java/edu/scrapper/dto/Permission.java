package edu.scrapper.dto;

public record Permission(
        String role,
        Object anyone
) {}
