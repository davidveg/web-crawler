package com.marfeel.web.crawler.boundary.client.rest;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.marfeel.web.crawler.model.RequestEvaluation;
import com.marfeel.web.crawler.processor.creator.ThreadPoolService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("marfeel/web/crawler/v1")
public class RestController {

	Logger LOG = LoggerFactory.getLogger(RestController.class);
	@Autowired
	private ThreadPoolService service;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = { "/receive" }, method = RequestMethod.POST)
	public void postUrlToEvaluate(@RequestBody RequestURLMapper[] requestsEvalutation, HttpServletRequest request)
			throws ParseException {
		try {
			for (RequestURLMapper obj : requestsEvalutation) {
				RequestEvaluation req = new RequestEvaluation(obj.getUrl(), obj.getRank());
				service.fire(req);
			}
		} catch (TaskRejectedException te) {
			LOG.error("Error - Max tasks reached" + te);
		}
	}
}
