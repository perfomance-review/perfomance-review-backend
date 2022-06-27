package ru.hh.performance_review.service.security;

import org.aspectj.lang.ProceedingJoinPoint;
import ru.hh.performance_review.security.context.ContextPerformanceReviewDto;

public interface ContextPerformanceReviewService {

    ContextPerformanceReviewDto buildContextPerformanceReviewDto(ProceedingJoinPoint joinPoint);
}
