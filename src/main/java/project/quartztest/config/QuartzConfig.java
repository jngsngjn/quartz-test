package project.quartztest.config;

import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

	private final AutowireCapableBeanFactory beanFactory;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setJobFactory(jobFactory()); // JobFactory 설정
		factory.setOverwriteExistingJobs(true); // 기존 Job 덮어쓰기 허용
		factory.setAutoStartup(true);

		Properties properties = new Properties();
		// TODO 설정 파일에서 읽도록 개선
		properties.setProperty("org.quartz.threadPool.threadCount", "20");
		factory.setQuartzProperties(properties);
		return factory;
	}

	@Bean
	public AutowiringSpringBeanJobFactory jobFactory() {
		return new AutowiringSpringBeanJobFactory(beanFactory);
	}

	@Bean
	public Scheduler scheduler(SchedulerFactoryBean factoryBean) {
		return factoryBean.getScheduler();
	}
}