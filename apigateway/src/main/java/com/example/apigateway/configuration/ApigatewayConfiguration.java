package com.example.apigateway.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.apigateway.filter.RequestLoggingFilter;

import jakarta.servlet.Filter;

@Configuration
public class ApigatewayConfiguration {

	@Bean
	public FilterRegistrationBean<Filter> loggingFilter() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RequestLoggingFilter());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
