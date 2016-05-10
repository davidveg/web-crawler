package com.marfeel.web.crawler.processor.creator;

import com.marfeel.web.crawler.model.RequestEvaluation;

public interface ServicePool {

	public void fire(final RequestEvaluation parameter);
}
