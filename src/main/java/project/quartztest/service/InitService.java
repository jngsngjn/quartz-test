package project.quartztest.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import project.quartztest.dto.DefaultJobDto;
import project.quartztest.job.TestJob;

import static project.quartztest.job.TestJob.KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitService {

    private final Scheduler scheduler;
    private final QuartzService quartzService;

    public static final String JOB_SUFFIX = "_job";
    public static final String TRIGGER_SUFFIX = "_trigger";
    public static final String DELIMITER = "_";

    @PostConstruct
    public void init() {
        DefaultJobDto jobDto = createDefaultJobDto(TestJob.class, createSimpleScheduleBuilder(3));
        quartzService.addQuartzJob(jobDto.getJobDetail(), jobDto.getTrigger());
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static DefaultJobDto createDefaultJobDto(Class<? extends Job> jobClass, ScheduleBuilder<?> scheduleBuilder) {
        return DefaultJobDto.builder()
                .jobClass(jobClass)
                .scheduleBuilder(scheduleBuilder)
                .jobDetail(createDefaultJobDetail(jobClass, createJobKey(jobClass)))
                .trigger(createDefaultJobTrigger(createTriggerKey(jobClass), scheduleBuilder))
                .build();
    }

    public static SimpleScheduleBuilder createSimpleScheduleBuilder(int repeatInterval) {
        return SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(repeatInterval) // 반복 주기 설정
                .repeatForever();
    }

    private static JobDetail createDefaultJobDetail(Class<? extends Job> jobClass, JobKey jobKey) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(KEY, 1);
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobKey)
                .setJobData(jobDataMap)
                .build();
    }

    private static Trigger createDefaultJobTrigger(TriggerKey triggerKey, ScheduleBuilder<?> scheduleBuilder) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .build();
    }

    public static JobKey createJobKey(Class<? extends Job> jobClass, String... value) {
        return new JobKey(generateKey(jobClass, JOB_SUFFIX, value));
    }

    public static TriggerKey createTriggerKey(Class<? extends Job> jobClass, String... value) {
        return new TriggerKey(generateKey(jobClass, TRIGGER_SUFFIX, value));
    }

    private static String generateKey(Class<? extends Job> jobClass, String suffix, String... value) {
        StringBuilder result = new StringBuilder(jobClass.getSimpleName());

        if (value.length > 0) {
            result.append(DELIMITER).append(String.join(DELIMITER, value));
        }

        result.append(suffix);
        return result.toString();
    }
}