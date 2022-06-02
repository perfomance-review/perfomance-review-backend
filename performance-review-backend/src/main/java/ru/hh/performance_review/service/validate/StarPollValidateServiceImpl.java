package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;
import ru.hh.performance_review.service.validate.utils.Utils;

import java.util.List;

@Slf4j
@Service
public class StarPollValidateServiceImpl implements StarPollValidateService {

    @Override
    public void validateDataStartPoll(String pollId, String userId, List<String> includedIds) {

        Utils.validateUuidAsString(pollId, "pollId");
        Utils.validateUuidAsString(userId, CookieConst.USER_ID);

        if (CollectionUtils.isEmpty(includedIds)) {
            String errorMsg = String.format("body request %s==null or empty", "includedIdsString");
            log.error(errorMsg);
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR, errorMsg);
        }

        if (includedIds.size() < 2) {
            String errorMsg = String.format("body request %s contain < 2 ids", "includedIdsString");
            log.error(errorMsg);
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR, errorMsg);
        }

        includedIds.forEach(s -> Utils.validateUuidAsString(s, "includedId[" + includedIds.indexOf(s) + "]"));
    }
}
