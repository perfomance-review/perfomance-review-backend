package ru.hh.performance_review.service.validate;

import org.springframework.stereotype.Service;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;
import ru.hh.performance_review.service.validate.utils.Utils;

@Service
public class RatingRequestValidateServiceImpl implements RatingRequestValidateService {

    @Override
    public void validateUpdateWinnerRequestDto(String userId, UpdateWinnerRequestDto updateWinnerRequestDto) {

        Utils.validateUuidAsString(updateWinnerRequestDto.getPerson1Id(), UpdateWinnerRequestDto.Fields.person1Id);
        Utils.validateUuidAsString(updateWinnerRequestDto.getPerson2Id(), UpdateWinnerRequestDto.Fields.person2Id);
        Utils.validateUuidAsString(updateWinnerRequestDto.getPollId(), UpdateWinnerRequestDto.Fields.pollId);
        Utils.validateUuidAsString(updateWinnerRequestDto.getQuestionId(), UpdateWinnerRequestDto.Fields.questionId);
        Utils.validateUuidAsString(updateWinnerRequestDto.getWinnerId(), UpdateWinnerRequestDto.Fields.winnerId);

        Utils.validateUuidAsString(userId, CookieConst.USER_ID);
    }
}
