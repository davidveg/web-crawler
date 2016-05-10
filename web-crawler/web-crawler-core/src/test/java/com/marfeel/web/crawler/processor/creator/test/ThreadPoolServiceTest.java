package com.marfeel.web.crawler.processor.creator.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.marfeel.web.crawler.model.RequestEvaluation;
import com.marfeel.web.crawler.processor.creator.ProcessService;
import com.marfeel.web.crawler.processor.creator.ThreadPoolService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:web-crawler-core-test-boot-beans.xml")
public class ThreadPoolServiceTest {

	@Mock
	private TaskExecutor taskExecutor;

	@Mock
	private ProcessService processService;

	private ThreadPoolService threadPoolService;

	private RequestEvaluation re;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		threadPoolService = new ThreadPoolService(taskExecutor, processService);
		re = createRequestEvalutation();
	}

	@Test
	public void startThreadPoolSuccessTest() {
		threadPoolService.fire(re);
		Mockito.verify(taskExecutor).execute(Mockito.any(Runnable.class));
	}

	@Test
	public void startThreadPoolFailTest() throws IOException {
		Mockito.doThrow(new IOException()).when(processService).start(Mockito.same(re));
		threadPoolService.fire(re);
		Mockito.verify(taskExecutor).execute(Mockito.any(Runnable.class));
	}

	private RequestEvaluation createRequestEvalutation() {
		RequestEvaluation re = new RequestEvaluation();
		re.setUrl("www.gogle.com");
		re.setRank("3441");
		return re;
	}
}
