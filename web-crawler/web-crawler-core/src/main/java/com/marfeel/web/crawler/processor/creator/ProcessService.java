package com.marfeel.web.crawler.processor.creator;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.marfeel.web.crawler.infrastrucutre.repo.RequestEvaluationRepository;
import com.marfeel.web.crawler.model.RequestEvaluation;

@Component
public class ProcessService {

	@Autowired
	private String regex;

	private final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10";

	@Autowired
	private RequestEvaluationRepository rRepo;

	public ProcessService() {
	}

	public ProcessService(String regex, RequestEvaluationRepository rRepo) {
		this.regex = regex;
		this.rRepo = rRepo;
	}

	public void start(RequestEvaluation re) throws IOException {
		try {
			if (valid(re)) {
				String url = re.getUrl().contains("www.") ? re.getUrl() : "www." + re.getUrl();
				url = url.contains("http") ? url : "http://" + url;
				Response response = Jsoup.connect(url).userAgent(USER_AGENT).followRedirects(true).timeout(3000)
						.execute();
				Document doc = Jsoup.connect(response.url().toString()).userAgent(USER_AGENT).timeout(3000)
						.validateTLSCertificates(false).get();
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(doc.title());
				if (matcher.matches()) {
					// Its Mafelliazable, persists rank
					rRepo.save(re);
				}
			}
		} catch (IOException e) {
			throw e;
		}
	}

	private boolean valid(RequestEvaluation re) {
		if (re.getUrl() != null && !re.getUrl().replaceAll(" ", "").equals("")) {
			return true;
		} else
			return false;
	}
}
