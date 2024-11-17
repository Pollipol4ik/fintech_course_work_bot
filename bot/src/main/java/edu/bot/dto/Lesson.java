package edu.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Lesson {
    private UUID id;
    private String studentFullName;
    private Date date;
    private int duration;
    private String subject;
    private double price;
    private boolean isPaid;
}
