package com.marfeel.web.crawler.boundary.client.rest.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.marfeel.web.crawler.boundary.client.rest.GlobalExceptionHandler;
import com.marfeel.web.crawler.boundary.client.rest.RestController;
import com.marfeel.web.crawler.model.RequestEvaluation;
import com.marfeel.web.crawler.processor.creator.ServicePool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:web-crawler-boundary-test-boot-beans.xml")
public class RestControllerTest {

	@Mock
	private ServicePool service;

	@InjectMocks
	private RestController restController;

	private MockMvc mockMvc;

	private String json;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(restController).setControllerAdvice(new GlobalExceptionHandler()).build();
	}

	@Test
	public void receiveListOfUrlsSuccessTest() throws Exception {

		json = "[{\"url\": \"canda.com\"},{\"url\": \"toshiba.es\"}]";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/marfeel/web/crawler/v1/receive")
						.contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void jsonInvalidTest() throws Exception {

		json = "[{\"url\": canda.com},{\"url\": \"toshiba.es\"}]";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/marfeel/web/crawler/v1/receive")
						.contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	public void internalErrorTest() throws Exception {
		Mockito.doThrow(new RuntimeException("error...")).when(service).fire(Mockito.any(RequestEvaluation.class));
		json = "[{\"url\": \"canda.com\"},{\"url\": \"toshiba.es\"}]";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/marfeel/web/crawler/v1/receive")
						.contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	public void methodNotAllowedTest() throws Exception {

		json = "[{\"url\": \"canda.com\"},{\"url\": \"toshiba.es\"}]";
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/marfeel/web/crawler/v1/receive")
						.contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
	}
	
	@Test
	public void methodNotFoundTest() throws Exception {

		json = "[{\"url\": \"canda.com\"},{\"url\": \"toshiba.es\"}]";
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/marfeel/web/crawler/v1/received")
						.contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
