package edu.scrapper.dto;

public record PrimaryActionDetail(
        PermissionChange permissionChange,
        Create create,
        Move move
) {}