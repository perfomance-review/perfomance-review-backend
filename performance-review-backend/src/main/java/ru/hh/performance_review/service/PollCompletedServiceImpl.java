package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.PollDao;
import ru.hh.performance_review.dao.RespondentsOfPollDao;
import ru.hh.performance_review.model.Poll;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class PollCompletedServiceImpl implements PollCompletedService {
    private final PollDao pollDao;
    private final RespondentsOfPollDao respondentsOfPollDao;

    @Override
    @Transactional
    public void closePollsByDeadline() {
        List<Poll> polls = pollDao.findByDeadlineLessThan(LocalDate.now());
        int rowsChangedStatus = respondentsOfPollDao.setStatusByPolls(polls, PollStatus.CLOSED);
        log.info("Set status CLOSE for {} rows", rowsChangedStatus);
    }
}
