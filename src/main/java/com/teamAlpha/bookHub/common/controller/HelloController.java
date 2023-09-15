package com.teamAlpha.bookHub.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sailesh on 1/27/22.
 */
@RequestMapping("/public")
@RestController
public class HelloController {

	@GetMapping("/")
	public ResponseEntity hello() {
		return new ResponseEntity("Hello World", HttpStatus.OK);
	}
}
