package edu.scrapper.dto;

import java.util.List;

public record Move(
        List<Parent> addedParents
) {}