package edu.scrapper.dto;

import java.util.List;

public record DriveActivity(
        PrimaryActionDetail primaryActionDetail,
        List<Actor> actors,
        List<Action> actions,
        List<Target> targets,
        String timestamp
) {}
