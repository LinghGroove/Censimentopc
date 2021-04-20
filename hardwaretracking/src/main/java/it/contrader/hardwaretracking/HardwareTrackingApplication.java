package it.contrader.hardwaretracking;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import it.contrader.hardwaretracking.converter.ItemConverter;
import it.contrader.hardwaretracking.converter.LoanConverter;

@SpringBootApplication
public class HardwareTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HardwareTrackingApplication.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow_origin",
				"Content-Type", "Accept", "Jwt-Token", "Authorization", "Origin, Accept",
				"X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Access-Control-Allow_origin",
				"Content-Type", "Accept", "Jwt-Token", "Authorization", "Origin, Accept",
				"X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	} 
	
	@Bean
	public ItemConverter itemConverter() {
		return new ItemConverter();
	}
	
	@Bean
	public LoanConverter loanConverter() {
		return new LoanConverter();
	}
	
	

}
