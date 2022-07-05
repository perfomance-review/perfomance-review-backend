package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.consts.RequestParams;
import ru.hh.performance_review.dto.request.CreatePollRequestDto;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;
import ru.hh.performance_review.service.validate.utils.Utils;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PollValidateServiceImpl implements PollValidateService {

    public static final int QUESTIONS_MAX_LIMIT = 10;
    public static final int QUESTIONS_MIN_LIMIT = 1;
    public static final int RESPONDENTS_MAX_LIMIT = 10;
    public static final int RESPONDENTS_MIN_LIMIT = 3;

    @Override
    public void validatePollById(String userId, String pollId) {
        validateUserIdAndPollId(userId, pollId);
    }

    @Override
    public void validateComparePairsOfPoll(String userId, String pollId) {
        validateUserIdAndPollId(userId, pollId);
    }

    @Override
    public void validatePollsByUserId(String userId, Set<String> statuses) {
        Utils.validateUuidAsString(userId, RequestParams.USER_ID);
        Utils.validatePollStatusAsSetString(statuses, RequestParams.RESPONDENT_ID);
    }

    @Override
    public void validateCreatePollRequestDto(final CreatePollRequestDto request, final String userId) {
        Utils.validateUuidAsString(userId, RequestParams.USER_ID);

        if (request.getName() == null) {
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR,
                "Поле name не может быть пустым");

        }

        List<String> questionIds = request.getQuestionIds();
        if (questionIds == null || questionIds.size() < QUESTIONS_MIN_LIMIT) {
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR,
                String.format("Количество вопросов меньше %s", QUESTIONS_MIN_LIMIT));
        }
        if (questionIds.size() > QUESTIONS_MAX_LIMIT) {
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR,
                String.format("Количество вопросов больше %s: %s", QUESTIONS_MAX_LIMIT, questionIds.size()));
        }
        questionIds.forEach(o -> Utils.validateUuidAsString(o, RequestParams.QUESTION_ID));

        List<String> respondentIds = request.getRespondentIds();
        if (respondentIds == null || respondentIds.size() < RESPONDENTS_MIN_LIMIT) {
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR,
                String.format("Количество респондентов меньше %s", RESPONDENTS_MIN_LIMIT));
        }
        if (respondentIds.size() > RESPONDENTS_MAX_LIMIT) {
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR,
                String.format("Количество респондентов больше %s: %s", RESPONDENTS_MAX_LIMIT, questionIds.size()));
        }
        respondentIds.forEach(o -> Utils.validateUuidAsString(o, RequestParams.RESPONDENT_ID));

        Utils.validateDateAsString(request.getDeadline(), "deadline");
        LocalDate deadline = LocalDate.parse(request.getDeadline());
        if (LocalDate.now().isAfter(deadline)) {
            throw new ValidateException(InternalErrorCode.VALIDATION_ERROR,
                String.format("Некорректно заполнено поле %s: %s", "deadLine", deadline));
        }
    }

    private void validateUserIdAndPollId(String userId, String pollId) {
        Utils.validateUuidAsString(userId, RequestParams.USER_ID);
        Utils.validateUuidAsString(pollId, RequestParams.POLL_ID);
    }
}
