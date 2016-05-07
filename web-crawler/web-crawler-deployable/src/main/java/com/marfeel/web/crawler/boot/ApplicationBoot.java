package com.marfeel.web.crawler.boot;

import javax.naming.NamingException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ImportResource({ "web-crawler-infrastructure-boot-beans.xml", "web-crawler-persistence-boot-beans.xml",
		"web-crawler-boundary-boot-beans.xml" })
public class ApplicationBoot {

	public static void main(String[] args) throws NamingException {
		SpringApplication.run(new Object[] { ApplicationBoot.class }, args);
	}

	@Override
	public String toString() {
		return "pegasus";
	}

}