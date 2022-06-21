package ru.hh.performance_review.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для проверки прав доступа REST сервисах
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PerformanceReviewSecured {

    /**
     * @return Строковый имя ролей, для вызова метода
     */
    String[] roles() default "";

}

