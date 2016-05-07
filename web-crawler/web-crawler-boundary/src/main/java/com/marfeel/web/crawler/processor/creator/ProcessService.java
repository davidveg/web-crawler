package com.marfeel.web.crawler.processor.creator;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.marfeel.web.crawler.boundary.client.rest.RequestEvaluationRepository;
import com.marfeel.web.crawler.model.RequestEvaluation;

@Component
public class ProcessService {

	private final Logger LOG = LoggerFactory.getLogger(ProcessService.class);

	@Autowired
	private String regex;

	@Autowired
	private RequestEvaluationRepository rRepo;

	public void start(RequestEvaluation re) {
		try {
			String url = (re.getUrl().contains("http")) ? re.getUrl() : "http://" + re.getUrl();
			Response response = Jsoup.connect(url).followRedirects(true).timeout(3000).execute();
			Document doc = Jsoup.connect(response.url().toString())
					.userAgent(
							"Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10")
					.timeout(3000).validateTLSCertificates(false).get();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(doc.title());
			if (matcher.matches()) {
				// Its Mafelliazable, persists rank
				rRepo.save(re);
			}
		} catch (IOException e) {
			LOG.error("http error:" + e);
		}

	}
}
