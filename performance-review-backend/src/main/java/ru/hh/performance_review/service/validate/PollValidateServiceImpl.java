package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.consts.RequestParams;
import ru.hh.performance_review.service.validate.utils.Utils;

@Slf4j
@Service
public class PollValidateServiceImpl implements PollValidateService {

    @Override
    public void getPollByIdValidate(String userId, String pollId) {
        validateUserIdAndPollId(userId, pollId);
    }

    @Override
    public void validateComparePairsOfPoll(String userId, String pollId) {
        validateUserIdAndPollId(userId, pollId);
    }

    private void validateUserIdAndPollId(String userId, String pollId){
        Utils.validateUuidAsString(userId, RequestParams.USER_ID);
        Utils.validateUuidAsString(pollId, RequestParams.POLL_ID);
    }
}
