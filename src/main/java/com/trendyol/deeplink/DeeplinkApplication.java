package com.trendyol.deeplink;

import com.trendyol.deeplink.repository.DeepLinkImpl;
import com.trendyol.deeplink.repository.DeepLinkRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeeplinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeeplinkApplication.class, args);
	}
}