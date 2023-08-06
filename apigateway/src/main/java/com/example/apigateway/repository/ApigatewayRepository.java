package com.example.apigateway.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.apigateway.entity.ApigatewayLog;

import jakarta.transaction.Transactional;

public interface ApigatewayRepository extends JpaRepository<ApigatewayLog, Long>{

	@Query("SELECT a FROM ApigatewayLog a WHERE :startDateTime >= a.logDate AND a.logDate <= :endDateTime")
	List<ApigatewayLog> findAllByPeriod(LocalDate startDateTime, LocalDate endDateTime);

	@Modifying
	@Transactional
	@Query("DELETE FROM ApigatewayLog a WHERE :startDateTime >= a.logDate AND a.logDate <= :endDateTime")
	void deleteAllByPeriod(LocalDate startDateTime, LocalDate endDateTime);

	@Modifying
	@Transactional
	@Query("DELETE FROM ApigatewayLog a WHERE :startDateTime >= a.logDate AND a.logDate <= :endDateTime AND a.requestInitial IS NOT NULL")
	void deleteAllResponseByPeriod(LocalDate startDateTime, LocalDate endDateTime);
}
