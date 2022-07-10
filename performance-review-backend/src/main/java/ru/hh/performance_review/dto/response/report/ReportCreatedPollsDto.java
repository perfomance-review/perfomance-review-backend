package ru.hh.performance_review.dto.response.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * Предназначена для вывода информации по отчету "Созданные отчеты"
 */
@Data
@Accessors(chain = true)
public class ReportCreatedPollsDto {

    private String name;
    private String description;
    private LocalDate createdDt;
    private LocalDate deadLineDt;
    private String status;
}
