package project.quartztest.config;

import lombok.RequiredArgsConstructor;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Quartz Job 클래스 DI를 위해 재정의
 */
@RequiredArgsConstructor
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

	private final AutowireCapableBeanFactory beanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object job = super.createJobInstance(bundle);
		beanFactory.autowireBean(job); // 생성된 Job 인스턴스에 의존성 주입
		return job;
	}
}