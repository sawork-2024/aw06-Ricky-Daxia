package com.example.posproduct;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
@EnableDiscoveryClient
public class PosProductApplication {

	public static void main(String[] args) {
		if (args.length > 0) {
			String port = args[0];
			System.setProperty("server.port", port);
		}
		SpringApplication.run(PosProductApplication.class, args);
	}

	@Bean 
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
		TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
				.timeoutDuration(Duration.ofSeconds(4))
				.build();
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
				.failureRateThreshold(50)
				.waitDurationInOpenState(Duration.ofMillis(1000))
				.slidingWindowSize(2)
				.build();

		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.timeLimiterConfig(timeLimiterConfig)
				.circuitBreakerConfig(circuitBreakerConfig)
				.build());
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration1() {
		TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
				.timeoutDuration(Duration.ofSeconds(4))
				.build();
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
				.failureRateThreshold(50)
				.waitDurationInOpenState(Duration.ofMillis(1000))
				.slidingWindowSize(2)
				.build();

		return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
				.timeLimiterConfig(timeLimiterConfig).build(), "circuitBreaker");
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration2() {
		TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
				.timeoutDuration(Duration.ofSeconds(4))
				.build();
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
				.failureRateThreshold(50)
				.waitDurationInOpenState(Duration.ofMillis(1000))
				.slidingWindowSize(2)
				.build();

		return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
						.timeLimiterConfig(timeLimiterConfig).build(),
				"circuitBreaker1", "circuitBreaker2", "circuitBreaker3");
	}

}
