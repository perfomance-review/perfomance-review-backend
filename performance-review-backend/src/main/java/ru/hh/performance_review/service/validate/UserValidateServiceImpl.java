package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;

import java.util.UUID;

@Slf4j
@Service
public class UserValidateServiceImpl implements UserValidateService {

    @Override
    public void userIdValidate(String userId) {
        if (StringUtils.isBlank(userId)) {
            String errorMsg = "cookie params user-id==null";
            log.error(errorMsg);
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR, errorMsg);
        }
        try {
            UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new ValidateException(e, InternalErrorCode.VALIDATION_ERROR,
                    String.format("userId:%s", userId));
        }
    }
}
