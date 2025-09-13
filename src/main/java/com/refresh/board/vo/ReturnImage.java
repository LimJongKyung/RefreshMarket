package com.refresh.board.vo;

import java.sql.Date;

public class ReturnImage {
	
	private Long imageId;
    private Long returnId;
    private byte[] imageData;
    private String imageName;
    private Date uploadDate;
    private String imageBase64;
    
    // Getters and Setters
    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getReturnId() {
        return returnId;
    }
    public void setReturnId(Long returnId) {
        this.returnId = returnId;
    }

    public byte[] getImageData() {
        return imageData;
    }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
    public String getImageBase64() {
        return imageBase64;
    }
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
