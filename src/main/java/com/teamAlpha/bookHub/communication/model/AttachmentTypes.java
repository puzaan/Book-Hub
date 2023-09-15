package com.teamAlpha.bookHub.communication.model;

public enum AttachmentTypes {

	LOGO(1, "LOGO"), 
	BANNER(2, "BANNER"), 
	PRODUCT(3,"PRODUCT"), 
	CATEGORY(4, "CATEGORY"), 
	AVATAR(5, "AVATAR");

	private Integer key;
	private String description;

	AttachmentTypes(int key, String description) {
		this.key = key;
		this.description = description;
	}

	public Integer getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

	public static AttachmentTypes lookUpByKey(Integer key) {
		for (AttachmentTypes types : AttachmentTypes.values()) {
			if (types.getKey() == key) {
				return types;
			}
		}
		return null;
	}

}
