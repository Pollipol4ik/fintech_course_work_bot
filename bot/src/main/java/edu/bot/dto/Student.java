package edu.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Student {
    private UUID id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String contactInfo;
}
