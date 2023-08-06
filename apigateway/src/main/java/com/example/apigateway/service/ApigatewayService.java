package com.example.apigateway.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.entity.ApigatewayLog;
import com.example.apigateway.logType.LogType;
import com.example.apigateway.repository.ApigatewayRepository;

@Service
public class ApigatewayService {

	@Autowired
	private ApigatewayRepository repository;

	public ApigatewayLog saveRequest(ServerWebExchange exchange) {
		ApigatewayLog entity = new ApigatewayLog();
		entity.setLogDate(LocalDate.now());
		entity.setHttpMethod(exchange.getRequest().getMethod().name());
		entity.setLogType(LogType.REQUEST);
		entity.setRequestedUrl(exchange.getRequest().getURI().toString());

		return this.repository.save(entity);
	}

	public void saveResponse(ServerWebExchange exchange, ApigatewayLog request) {
		ApigatewayLog entity = new ApigatewayLog();
		entity.setLogDate(LocalDate.now());
		entity.setHttpMethod(exchange.getRequest().getMethod().name());
		entity.setLogType(LogType.RESPONSE);
		entity.setRequestedUrl(exchange.getRequest().getURI().toString());
		entity.setRequestInitial(request);

		this.repository.save(entity);
	}

	public void doBackupOfRequests() throws IOException {
		LocalDate yesterday = LocalDate.now().minusDays(1);

		LocalDateTime startDateTime = yesterday.atStartOfDay();
		LocalDateTime endDateTime = yesterday.atTime(LocalTime.MAX);

		List<ApigatewayLog> logs = this.repository.findAllByPeriod(startDateTime.toLocalDate(), endDateTime.toLocalDate());
		StringBuilder content = new StringBuilder();
		logs.forEach(log -> {
			content.append(log.getLogDate());
			content.append(" - ");
			content.append(log.getHttpMethod());
			content.append(" - ");
			content.append(log.getLogType().name());
			content.append(" - ");
			content.append(log.getRequestedUrl());
			content.append(System.lineSeparator());
		});

		File tempFile = File.createTempFile(yesterday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "_apigateway_backup", ".txt");

		Path path = tempFile.toPath();
		Files.write(path, content.toString().getBytes());

		this.repository.deleteAllResponseByPeriod(yesterday, yesterday);
		this.repository.deleteAllByPeriod(startDateTime.toLocalDate(), endDateTime.toLocalDate());
	}
}
