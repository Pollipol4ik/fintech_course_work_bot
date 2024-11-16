package edu.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Payment {
    private UUID id;
    private UUID lessonId;
    private Date paymentDate;
}
