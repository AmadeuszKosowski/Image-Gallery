package com.example.gallery.model;

public class GalleryImage {

	private String fileNumber;
	private String displayName;
	private String image;
	private String miniature;

	public GalleryImage() {
	}

	public GalleryImage(String fileNumber, String displayName) {
		this.fileNumber = fileNumber;
		this.displayName = displayName;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getMiniature() {
		return miniature;
	}

	public void setMiniature(String miniature) {
		this.miniature = miniature;
	}
}
