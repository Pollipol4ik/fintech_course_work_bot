package edu.scrapper.dto;

public record ActionDetail(
        PermissionChange permissionChange,
        Create create,
        Move move
) {}

