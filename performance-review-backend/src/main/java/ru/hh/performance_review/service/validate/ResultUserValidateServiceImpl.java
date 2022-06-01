package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.service.validate.utils.Utils;

import java.util.List;

@Service
@Slf4j
public class ResultUserValidateServiceImpl implements ResultUserValidateService {

    @Override
    public void validateDataResultUser(String pollId, String userId) {

        Utils.validateUuidAsString(pollId, "pollId");
        Utils.validateUuidAsString(userId, CookieConst.USER_ID);

    }
}
