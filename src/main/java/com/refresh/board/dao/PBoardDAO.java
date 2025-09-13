package com.refresh.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.board.vo.ProductDetailImageVO;
import com.refresh.board.vo.ProductImageVO;
import com.refresh.board.vo.ReturnImage;
import com.refresh.board.vo.ReturnRequest;
import com.refresh.board.vo.ReviewImageVO;

@Mapper
public interface PBoardDAO {
	// 상품 리스트
	List<ProductBoardVO> productboard();

	// 리뷰
	List<PReviewVO> getPagedReviewsByProductId(Map<String, Object> paramMap);
	int getReviewCountByProductId(int productId);
	
	List<PReviewVO> getReviewsPaged(Map<String, Object> paramMap);
    List<ReviewImageVO> getImagesByReviewId(Long reviewId);
    int getTotalReviewCountByUser(Map<String, Object> paramMap);

    ProductBoardVO getProductById(int productId);
    
    List<ProductBoardVO> getSaleProducts();
    List<ProductBoardVO> getGroupProducts();
    List<ProductBoardVO> getPromotionProducts();
    
    List<ProductBoardVO> searchProducts(String keyword);
    
    void insertGuestOrder(OrderVO order);
    
    // 상품 주문 정보 보기
    List<OrderVO> getOrderInfoByCustomerId(String customerId);
    ProductBoardVO getProductImageById(String productId);
    
    // 상품 메인 추가 이미지
    List<ProductImageVO> getImagesByProductId(int productId); 
    
    // 새로 추가: 상세 이미지용
    List<ProductDetailImageVO> getDetailImagesByProductId(int productId);
    
    OrderVO findById(Long orderId);
    
    void updateOrderStatus(@Param("orderId") Long orderId,
            @Param("isCanceled") String isCanceled);
    
    Long getNextReturnId();
    void insertReturnRequest(ReturnRequest request);
    void insertReturnImage(ReturnImage image);
    
    List<ReturnRequest> selectReturnRequestsByOrderId(Long orderId);
    List<ReturnImage> selectReturnImages(Long returnId);
    
    void insertReview(PReviewVO review);
    void insertReviewImage(ReviewImageVO image);
    
    PReviewVO getReviewById(Long reviewId);
    void deleteReview(Long reviewId);
    void deleteReviewImages(Long reviewId);
    Long getProductIdByReviewId(Long reviewId);
    
    void updateReviewComment(Long reviewId, String reviewComment);
    void updateReviewTimestamp(Long reviewId);
}
