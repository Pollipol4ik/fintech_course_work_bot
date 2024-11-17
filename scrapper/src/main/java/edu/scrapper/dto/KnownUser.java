package edu.scrapper.dto;


public record KnownUser(
        String personName,
        boolean isCurrentUser
) {}