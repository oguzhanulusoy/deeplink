package com.trendyol.deeplink.repository;

public interface DeepLinkRepository {
	
	String getDeepLinkRequest(String urlString);
	
	String getWebUrlRequest(String urlString);
	
}
