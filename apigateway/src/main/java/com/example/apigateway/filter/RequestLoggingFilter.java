package com.example.apigateway.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.entity.ApigatewayLog;
import com.example.apigateway.service.ApigatewayService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered, Filter {

	@Autowired
	private ApigatewayService service;

	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		logger.info("Request received. Method: " + exchange.getRequest().getMethod() + ", URL: " + exchange.getRequest().getURI());
		ApigatewayLog request = this.service.saveRequest(exchange);

		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			if (exchange.getRequest().getPath().toString().startsWith("/email")) {
				logger.info("Request processed. Status: " + exchange.getResponse().getStatusCode());
				this.service.saveResponse(exchange, request);
			}
		}));
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	}
}
