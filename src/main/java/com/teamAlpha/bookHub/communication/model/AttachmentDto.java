package com.teamAlpha.bookHub.communication.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

public class AttachmentDto extends RepresentationModel<AttachmentDto> {

	private Integer attachmentId;
	private Integer attachmentTypeId;
	private String uploadedBy;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date uploadedOn;
	private Integer shopId;
	private String fileHash;

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getAttachmentTypeId() {
		return attachmentTypeId;
	}

	public void setAttachmentTypeId(Integer attachmentTypeId) {
		this.attachmentTypeId = attachmentTypeId;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Date getUploadedOn() {
		return uploadedOn;
	}

	public void setUploadedOn(Date uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	@Override
	public String toString() {
		return "AttachmentDto [attachmentId=" + attachmentId + ", attachmentTypeId=" + attachmentTypeId
				+ ", uploadedBy=" + uploadedBy + ", uploadedOn=" + uploadedOn + ", shopId=" + shopId + ", fileHash="
				+ fileHash + "]";
	}

}
