package ru.hh.performance_review.service.report;

import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;

public interface ReportDocumentService {

    ReportResponseContextDto createReportContext(ReportRequestContextDto reportRequestContextDto);
}
