package com.refresh.board.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProductBoardVO {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private byte[] image;
    private String imageType;  // ✅ 추가: 이미지 MIME 타입
    private String categoryId;
    private Date createdAt;
    private Date updatedAt;
    private String mainDisplay;
    private String seller;         // 판매자
    private String manufacturer;   // 제조업체
    private String detailOption;   // 상세선택 (콤마로 구분)
    private String detailOptionPrice;
    
    private List<ProductImageVO> images;
    
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getDetailOption() {
		return detailOption;
	}
	public void setDetailOption(String detailOption) {
		this.detailOption = detailOption;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainDisplay() {
		return mainDisplay;
	}
	public void setMainDisplay(String mainDisplay) {
		this.mainDisplay = mainDisplay;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public String getDetailOptionPrice() {
		return detailOptionPrice;
	}
	public void setDetailOptionPrice(String detailOptionPrice) {
		this.detailOptionPrice = detailOptionPrice;
	}
	public List<ProductImageVO> getImages() {
		return images;
	}
	public void setImages(List<ProductImageVO> images) {
		this.images = images;
	} 
}