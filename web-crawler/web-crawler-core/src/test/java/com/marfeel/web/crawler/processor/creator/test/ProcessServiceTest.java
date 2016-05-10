package com.marfeel.web.crawler.processor.creator.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.marfeel.web.crawler.infrastrucutre.repo.RequestEvaluationRepository;
import com.marfeel.web.crawler.model.RequestEvaluation;
import com.marfeel.web.crawler.processor.creator.ProcessService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:web-crawler-core-test-boot-beans.xml")
public class ProcessServiceTest {

	private String regex;

	@Mock
	private RequestEvaluationRepository rRepo;

	@InjectMocks
	private ProcessService processService;

	private RequestEvaluation re;

	@Before
	public void setup() {
		regex = ".*\\b(?i)google\\b.*";
		MockitoAnnotations.initMocks(this);
		processService = new ProcessService(regex, rRepo);
		re = createRequestEvalutation();
	}

	@Test
	public void regexAcceptanceSuccessfullTest() throws IOException {
		re.setUrl("www.google.com");
		processService.start(re);
		Mockito.verify(rRepo).save(Mockito.same(re));
	}

	@Test
	public void regexAcceptanceFailTest() throws IOException {
		re.setUrl("www.yahoo.com");
		processService.start(re);
		Mockito.verify(rRepo, Mockito.never()).save(Mockito.same(re));
	}

	@Test(expected = IOException.class)
	public void invalidURLAcceptanceFailTest() throws IOException {
		re.setUrl("413131313123.com");
		processService.start(re);
		Mockito.verify(rRepo, Mockito.never()).save(Mockito.same(re));
	}

	@Test
	public void urlBlankInvalidTest() throws IOException {
		re.setUrl("");
		processService.start(re);
		Mockito.verify(rRepo, Mockito.never()).save(Mockito.same(re));
	}

	@Test
	public void urlWithSpacesInvalidTest() throws IOException {
		re.setUrl("      ");
		processService.start(re);
		Mockito.verify(rRepo, Mockito.never()).save(Mockito.same(re));
	}

	@Test
	public void nullUrlInvalidTest() throws IOException {
		re.setUrl(null);
		processService.start(re);
		Mockito.verify(rRepo, Mockito.never()).save(Mockito.same(re));
	}

	private RequestEvaluation createRequestEvalutation() {
		RequestEvaluation re = new RequestEvaluation();
		re.setRank("3441");
		return re;
	}
}
