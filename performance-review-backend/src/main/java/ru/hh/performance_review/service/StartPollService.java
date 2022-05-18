package ru.hh.performance_review.service;

import ru.hh.performance_review.model.PollStatus;

import java.util.List;

public interface StartPollService {

    void changeStatusPoll(String pollId, String userId, PollStatus status);

    void saveExcluded(String pollId, String userId, List<String> includedIdsString);

}
