package project.quartztest.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
public class TestJob implements Job {

    public static final String KEY = "key";

    @Override
    public void execute(JobExecutionContext jobContext) {
        // JobDataMap jobDataMap = jobContext.getJobDetail().getJobDataMap();
        // int value = jobDataMap.getInt(KEY);
        // log.info("value: {}", value);
        //
        // jobDataMap.put(KEY, value + 1);
        log.info("TestJob execute");
    }
}