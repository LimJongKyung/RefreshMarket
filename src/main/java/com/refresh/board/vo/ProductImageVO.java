package com.refresh.board.vo;

import lombok.Data;

@Data
public class ProductImageVO {
    private int productId;     // PRODUCT_ID
    private byte[] image;      // IMAGE (BLOB)
    private String imageType;  // IMAGE_TYPE (MIME 타입, 예: image/jpeg)
    private int id;            // ID (고유 식별자)
    
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
