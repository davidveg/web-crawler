package com.marfeel.web.crawler.processor.creator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.marfeel.web.crawler.model.RequestEvaluation;

@Service(value = "threadPoolService")
public class ThreadPoolService implements ServicePool {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private TaskExecutor taskExecutor;
	private ProcessService processService;

	public ThreadPoolService(TaskExecutor taskExecutor, ProcessService processService) {
		this.taskExecutor = taskExecutor;
		this.processService = processService;
	}

	public void fire(final RequestEvaluation parameter) {

		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					processService.start(parameter);
				} catch (IOException e) {
					LOG.error("http error:" + e);
				}
			}
		});
	}
}
