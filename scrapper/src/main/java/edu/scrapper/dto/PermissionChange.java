package edu.scrapper.dto;

import java.util.List;

public record PermissionChange(
        List<Permission> addedPermissions,
        List<Permission> removedPermissions
) {
}
