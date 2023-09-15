package com.teamAlpha.bookHub.communication.exception;

public class AttachmentDetailNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AttachmentDetailNotFoundException(String message) {
		super(message);
	}
	
	public AttachmentDetailNotFoundException (Integer id) {
		super("Attachment detail not found with id "+id);
	}
}
