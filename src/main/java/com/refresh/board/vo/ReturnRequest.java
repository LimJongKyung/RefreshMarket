package com.refresh.board.vo;

import java.util.Date;
import java.util.List;

public class ReturnRequest {
	private Long returnId;
    private Long orderId;
    private String memberId;
    private String reasonType;
    private String reasonText;
    private Date requestDate;
    private String productId;
    private String status;
    private String rejectReason;

 // 🔧 이미지 리스트 추가
    private List<ReturnImage> images;

    // Getter & Setter for images
    public List<ReturnImage> getImages() {
        return images;
    }

    public void setImages(List<ReturnImage> images) {
        this.images = images;
    }
    
    // Getters and Setters
    public Long getReturnId() {
        return returnId;
    }
    public void setReturnId(Long returnId) {
        this.returnId = returnId;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReasonType() {
        return reasonType;
    }
    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonText() {
        return reasonText;
    }
    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Date getRequestDate() {
        return requestDate;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
