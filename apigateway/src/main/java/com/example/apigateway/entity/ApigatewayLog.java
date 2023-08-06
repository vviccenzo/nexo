package com.example.apigateway.entity;

import java.time.LocalDate;

import com.example.apigateway.logType.LogType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class ApigatewayLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "log_date")
	private LocalDate logDate;

	@Column(name = "requested_url")
	private String requestedUrl;

	@OneToOne(orphanRemoval = true)
	private ApigatewayLog requestInitial;

	@Column(name = "http_method")
	private String httpMethod;

	@Column(name = "log_type")
	private LogType logType;
}
