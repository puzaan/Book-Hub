package com.teamAlpha.bookHub.communication.model;

import org.springframework.beans.factory.annotation.Value;

public class AttachmentStorageProperties {
	
	private final String PATH = "attachments";
	
//	@Value("${spring.servlet.multipart.location}")
//	private String PATH;
//
	public String getPATH() {
		return PATH;
	}

}
