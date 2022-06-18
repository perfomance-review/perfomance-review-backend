package ru.hh.performance_review.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.service.PollCompletedService;


@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledService {
    private final PollCompletedService pollCompletedService;

    @Scheduled(cron = "0 0 0 * * *")
    public void closePollsByDeadlineAtMidnight() {
        pollCompletedService.closePollsByDeadline();

    }
}
