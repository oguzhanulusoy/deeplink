package com.trendyol.deeplink.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.trendyol.deeplink.repository.DeepLinkRepository;

@RestController
@RequestMapping("/deeplink")
public class DeepLinkAPI {

	@Autowired
	DeepLinkRepository deepLinkRepository;

	@RequestMapping(value = "/deeplink/{path}", method = RequestMethod.GET)
	public ResponseEntity<?> getDeepLink(@PathVariable("path") String path) {
		String result = deepLinkRepository.getDeepLinkRequest(path);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/weburl/{path}", method = RequestMethod.GET)
	public ResponseEntity<?> getWebUrl(@PathVariable("path") String path) {
		String result = deepLinkRepository.getWebUrlRequest(path);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
