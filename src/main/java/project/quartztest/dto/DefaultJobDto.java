package project.quartztest.dto;

import lombok.Builder;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;

/**
 * 애플리케이션 기동 시 기본적으로 등록되어야 하는 Quartz Job을 위한 DTO
 */
@Data
@Builder
public class DefaultJobDto {
	private Class<? extends Job> jobClass;
	private ScheduleBuilder<?> scheduleBuilder;
	private JobDetail jobDetail;
	private Trigger trigger;
}