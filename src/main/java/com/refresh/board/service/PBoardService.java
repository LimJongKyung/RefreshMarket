package com.refresh.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.board.vo.ReturnImage;
import com.refresh.board.vo.ReturnRequest;
import com.refresh.board.vo.ReviewImageVO;

public interface PBoardService {
	// 계절 상품
	List<ProductBoardVO> productboard();

	// 상품에 대한 리뷰 목록 조회
	List<PReviewVO> getPagedReviewsByProductId(Map<String, Object> paramMap);
	int getReviewCountByProductId(int productId);
	
	// 따로 리뷰 페이징
	List<PReviewVO> getReviewsPaged(Map<String, Object> paramMap);
    List<ReviewImageVO> getImagesByReviewId(Long reviewId);
    int getTotalReviewCountByUser(Map<String, Object> paramMap); // 리뷰 존
	
    ProductBoardVO getProductById(int productId); // 상세  + 이미지용
    
    List<ProductBoardVO> getSaleProducts();
    List<ProductBoardVO> getGroupProducts();
    List<ProductBoardVO> getPromotionProducts();

    public List<ProductBoardVO> searchProducts(String keyword);
    
    void placeGuestOrder(OrderVO order);
    
    // 고객 ID로 주문 정보 조회
    List<OrderVO> getOrderInfoByCustomerId(String customerId);

    // 주문에 따른 상품 정보 가져오기
    ProductBoardVO getProductById(String productId);
    
    // 상품 디테일 이미지
    List<String> getDetailImagesByProductId(int productId);
    
    void cancelOrder(Long orderId, String isCanceled);
    
    void createReturnRequest(ReturnRequest request, List<ReturnImage> images);
    
    List<ReturnRequest> getReturnRequestsByOrderId(Long orderId);
    List<ReturnImage> getReturnImagesByReturnId(Long returnId);
    
    void insertReviewWithImages(PReviewVO review, List<ReviewImageVO> images);
    
    boolean deleteReview(Long reviewId, String userId, String userGrade);
    Long getProductIdByReviewId(Long reviewId);
    PReviewVO getReviewById(Long reviewId);
    void updateReview(Long reviewId, Long productId, String reviewComment, MultipartFile[] newImages);
}
