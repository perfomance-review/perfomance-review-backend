package ru.hh.performance_review.security.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.CallContext;
import ru.hh.performance_review.security.context.ContextPerformanceReviewDto;
import ru.hh.performance_review.security.context.SecurityContext;
import ru.hh.performance_review.security.exception.SecurityServiceException;
import ru.hh.performance_review.service.security.ContextPerformanceReviewService;
import ru.hh.performance_review.service.security.PerformanceReviewSecurityService;


/**
 * Аспект для проверки прав на вызов контроллеров
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class PerformanceReviewSecurityAspect {

    private final PerformanceReviewSecurityService performanceReviewSecurityService;
    private final ContextPerformanceReviewService contextPerformanceReviewService;

    /**
     * Метод обрабатывает ответ на запрос
     *
     * @param joinPoint точка соединения
     * @return ответ на запрос
     */

    @Around("@annotation(ru.hh.performance_review.security.annotation.PerformanceReviewSecured) && @annotation(javax.ws.rs.Path)")
    public Object secureHttpHandler(ProceedingJoinPoint joinPoint) throws Throwable {

        ContextPerformanceReviewDto contextPerformanceReviewDto = contextPerformanceReviewService.buildContextPerformanceReviewDto(joinPoint);
        CallContext callContext;
        try {
            callContext = performanceReviewSecurityService.getCallContextFromContextPerformanceReviewDto(contextPerformanceReviewDto);
            PerformanceReviewSecured performanceReviewSecured = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(PerformanceReviewSecured.class);
            performanceReviewSecurityService.checkPerformanceReviewSecuredAuthority(callContext, performanceReviewSecured);
        } catch (Exception e) {
            throw new SecurityServiceException(e.getMessage(), e);
        }

        try {
            SecurityContext.set(callContext);
            return joinPoint.proceed();
        } finally {
            SecurityContext.clear();
        }
    }

}
