package com.refresh.board.vo;

import java.util.Date;
import java.util.List;

public class PReviewVO {
    private int reviewId;
    private int productId;
    private String userName;
    private int rating;
    private String reviewComment;
    private Date createdAt;
    private Date updatedAt;
    private String userId;

    private List<ReviewImageVO> imageList;

    public List<ReviewImageVO> getImageList() {
        return imageList;
    }

    public void setImageList(List<ReviewImageVO> imageList) {
        this.imageList = imageList;
    }
    // Getters and Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	
    public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }     
}
