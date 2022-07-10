package ru.hh.performance_review.dto.response.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponseContextDto {

    private String reportName;

    private byte[] reportBytes;
}