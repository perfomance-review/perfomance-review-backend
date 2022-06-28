package ru.hh.performance_review.service.report;

public interface ReportGenerateService {

    byte[] getReport(String userId);
}
