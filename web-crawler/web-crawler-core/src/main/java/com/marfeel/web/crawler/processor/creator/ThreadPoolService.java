package com.marfeel.web.crawler.processor.creator;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.marfeel.web.crawler.model.RequestEvaluation;

@Service(value="threadPoolService")
public class ThreadPoolService implements ServicePool{

	private TaskExecutor taskExecutor;
	private ProcessService processService;

	public ThreadPoolService(TaskExecutor taskExecutor, ProcessService processService) {
		this.taskExecutor = taskExecutor;
		this.processService = processService;
	}

	public void fire(final RequestEvaluation parameter) {

		taskExecutor.execute(new Runnable() {
			public void run() {
				processService.start(parameter);
			}
		});
	}
}
