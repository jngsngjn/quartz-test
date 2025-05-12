package project.quartztest.controller;

import static project.quartztest.service.InitService.*;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.quartztest.job.TestJob;

@Slf4j
@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringHotDeployController {

	private final Scheduler scheduler;

	@GetMapping("/hot-deploy")
	public ResponseEntity<?> hotDeploy(@RequestParam int intervalMs) {
		try {
			String jobClassName = TestJob.class.getSimpleName();
			String jobKeyName = jobClassName + JOB_SUFFIX;
			String triggerKeyName = jobClassName + TRIGGER_SUFFIX;

			JobKey jobKey = JobKey.jobKey(jobKeyName);
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerKeyName);

			// 기존 트리거 조회
			Trigger oldTrigger = scheduler.getTrigger(triggerKey);

			if (oldTrigger == null) {
				log.warn("No trigger found for key: {}", triggerKey);
				return ResponseEntity.badRequest().body("Trigger not found");
			}

			SimpleScheduleBuilder newSchedule = createSimpleScheduleBuilder(intervalMs);
			Trigger newTrigger = createJobTrigger(jobKey, triggerKey, newSchedule);

			// 트리거 재설정
			scheduler.rescheduleJob(triggerKey, newTrigger);
			log.info("HotDeploy success: {} interval set to {}ms", jobClassName, intervalMs);
			return ResponseEntity.ok().build();
		} catch (SchedulerException e) {
			log.error("Failed to hot deploy job", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HotDeploy failed");
		}
	}
}