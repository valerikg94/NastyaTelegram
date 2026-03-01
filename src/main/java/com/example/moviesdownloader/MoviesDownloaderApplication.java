package com.example.moviesdownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {})
@EnableAutoConfiguration
public class MoviesDownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesDownloaderApplication.class, args);
		System.out.println("🚀 Telegram Bot с Spring Boot успешно запущен!");
	}

}
