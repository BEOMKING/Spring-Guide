package com.spring.guide.domain.member.infra;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MemberScheduler {

    @Scheduled(cron = "0/30 * * * * *")
    @SchedulerLock(name = "memberScheduler", lockAtMostFor = "PT29S", lockAtLeastFor = "PT29S")
    public void scheduler() {
        log.info("Scheduler is running at {}", LocalDateTime.now());
    }
}
