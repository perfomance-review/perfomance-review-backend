package ru.hh.performance_review.service.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.security.annotation.JwtTokenCookie;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.ContextPerformanceReviewDto;
import ru.hh.performance_review.security.exception.InvalidSecuredConfigurationException;
import ru.hh.performance_review.security.exception.SecuredErrorCode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class ContextPerformanceReviewServiceImpl implements ContextPerformanceReviewService {


    @Override
    public ContextPerformanceReviewDto buildContextPerformanceReviewDto(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();

        String jwtToken = getJwtToken(method, args);
        Set<String> roles = getRoles(method);

        return new ContextPerformanceReviewDto()
                .setJwtToken(jwtToken)
                .setRoles(roles);
    }

    /**
     * Получение roles из аннотации @PerformanceReviewSecured
     *
     * @param method - Метод {@code} предоставляет информацию об одном методе и доступ к нему
     * @return - Set<String>
     */
    private Set<String> getRoles(Method method) {
        Annotation[] methodAnnotations = method.getAnnotations();
        String[] roles = null;
        boolean isExistsAnnotationPerformanceReviewSecured = false;
        for (Annotation paramAnnotation : methodAnnotations) {
            if (paramAnnotation instanceof PerformanceReviewSecured) {
                isExistsAnnotationPerformanceReviewSecured = true;
                roles = ((PerformanceReviewSecured) paramAnnotation).roles();
                break;
            }
        }

        if (isExistsAnnotationPerformanceReviewSecured && roles.length == 0) {
            String errorMessage = String.format(SecuredErrorCode.INVALID_SECURED_CONFIGURATION.getErrorDescription(),
                    String.format("roles is NULL or EMPTY в аннотации @PerformanceReviewSecured метод:'%s'", method));
            log.error(errorMessage);
            throw new InvalidSecuredConfigurationException(errorMessage);
        }

        return roles == null ? Collections.emptySet() : new HashSet<>(Arrays.asList(roles));
    }

    /**
     * Получение сообщения(токена) из аннотации @JwtTokenCookie
     *
     * @param method - Метод {@code} предоставляет информацию об одном методе и доступ к нему
     * @param args   - массив аргументов в этой точке соединения
     * @return - сообщение
     */
    private String getJwtToken(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        String jwtToken = null;
        boolean isNotExistsAnnotationJwtTokenCookie = true;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation paramAnnotation : parameterAnnotations[argIndex]) {
                if (paramAnnotation instanceof JwtTokenCookie) {
                    isNotExistsAnnotationJwtTokenCookie = false;
                    jwtToken = (String) args[argIndex];
                    break;
                }
            }
        }

        if (isNotExistsAnnotationJwtTokenCookie) {
            String errorMessage = String.format(SecuredErrorCode.INVALID_SECURED_CONFIGURATION.getErrorDescription(),
                    String.format("Нет аннотации @JwtTokenCookie в методе :%s", method));
            log.error(errorMessage);
            throw new InvalidSecuredConfigurationException(errorMessage);
        }

        return jwtToken;
    }
}
