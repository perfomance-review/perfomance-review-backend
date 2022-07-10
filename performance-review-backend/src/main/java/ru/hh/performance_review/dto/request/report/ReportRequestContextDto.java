package ru.hh.performance_review.dto.request.report;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.hh.performance_review.consts.ReportType;

@Data
@Accessors(chain = true)
public class ReportRequestContextDto {

    private ReportType reportType;

    private String userId;
    private String pollId;
}
