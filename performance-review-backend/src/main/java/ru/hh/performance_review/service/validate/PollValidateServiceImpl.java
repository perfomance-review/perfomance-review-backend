package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;

import java.util.UUID;

@Slf4j
@Service
public class PollValidateServiceImpl implements PollValidateService {

    @Override
    public void pollIdValidate(String pollId) {
        if (StringUtils.isBlank(pollId)) {
            String errorMsg = "path params poll_id==null";
            log.error(errorMsg);
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR, errorMsg);
        }
        try {
            UUID.fromString(pollId);
        } catch (IllegalArgumentException e) {
            log.error(e.getLocalizedMessage());
            throw new ValidateException(e, InternalErrorCode.VALIDATION_ERROR,
                    String.format("pollId:%s", pollId));
        }
    }
}
