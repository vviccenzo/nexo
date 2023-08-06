package com.example.apigateway.schedule;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.apigateway.service.ApigatewayService;

@Component
public class ApigatewaySchedule {

	@Autowired
	private ApigatewayService service;

//	@Scheduled(cron = "0 0 3 * * ?")
	@Scheduled(fixedDelay = 1000, initialDelay = 3000)
	public void fixedDelaySch() throws IOException {
		this.service.doBackupOfRequests();
	}
}
