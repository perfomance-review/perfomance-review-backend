package ru.hh.performance_review.service.validate.utils;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;
import ru.hh.performance_review.model.PollStatus;

import java.util.Set;
import java.util.UUID;

@Slf4j
public class Utils {
    public static void validateUuidAsString(String checkField, String nameField) {
        if (StringUtils.isBlank(checkField)) {
            String errorMsg = String.format("path params %s==null or empty", nameField);
            log.error(errorMsg);
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR, errorMsg);
        }
        try {
            UUID.fromString(checkField);
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new ValidateException(e, InternalErrorCode.VALIDATION_ERROR,
                String.format("Некорректно заполнено поле %s:%s", nameField, checkField));
        }
    }
    public static void validatePollStatusAsSetString(Set<String> checkFields, String nameField) {
        if (checkFields == null || checkFields.isEmpty()) {
            String errorMsg = String.format("request params %s==null or empty", nameField);
            log.error(errorMsg);
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR, errorMsg);
        }
        for (String checkField : checkFields) {
            try {
                PollStatus.valueOf(checkField);
            } catch (IllegalArgumentException e) {
                log.error(e.getLocalizedMessage());
                throw new ValidateException(e, InternalErrorCode.VALIDATION_ERROR,
                    String.format("Некорректно заполнено поле %s:%s", nameField, checkField));
            }
        }
    }
}
