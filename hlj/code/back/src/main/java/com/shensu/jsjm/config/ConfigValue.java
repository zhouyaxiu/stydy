package com.shensu.jsjm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ConfigValue {

	private static Boolean encrypt;

	private static Boolean decrypt;

	private static Boolean samesite;

	public static Boolean getEncrypt() {
		return encrypt;
	}

	@Value("${custom.response.encrypt}")
	public void setEncrypt(Boolean encrypt) {
		ConfigValue.encrypt = encrypt;
	}

	public static Boolean getDecrypt() {
		return decrypt;
	}

	@Value("${custom.request.decrypt}")
	public void setDecrypt(Boolean decrypt) {
		ConfigValue.decrypt = decrypt;
	}

	public static Boolean getSamesite() {
		return samesite;
	}

	@Value("${custom.samesite}")
	public void setSamesite(Boolean samesite) {
		ConfigValue.samesite = samesite;
	}

	@Bean
	public TaskScheduler scheduledExecutorService() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("scheduled-thread-");
		return scheduler;
	}

}
