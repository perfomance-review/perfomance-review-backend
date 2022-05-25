package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.service.validate.utils.Utils;

@Slf4j
@Service
public class PollValidateServiceImpl implements PollValidateService {

    @Override
    public void getPollByIdValidate(String userId, String pollId) {
        Utils.validateUuidAsString(userId, "userId");
        Utils.validateUuidAsString(pollId, "poolId");
    }
}
