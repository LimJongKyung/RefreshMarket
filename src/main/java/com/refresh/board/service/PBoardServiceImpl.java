package com.refresh.board.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.refresh.board.dao.PBoardDAO;
import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.board.vo.ProductDetailImageVO;
import com.refresh.board.vo.ProductImageVO;
import com.refresh.board.vo.ReturnImage;
import com.refresh.board.vo.ReturnRequest;
import com.refresh.board.vo.ReviewImageVO;

@Service
public class PBoardServiceImpl implements PBoardService {
	
	 @Autowired
	 private PBoardDAO pBoardDAO;
	 // 계절 상품
	 @Override
	 public List<ProductBoardVO> productboard() {
	     return pBoardDAO.productboard();
	 }
	
	 @Override
	 public List<PReviewVO> getPagedReviewsByProductId(Map<String, Object> paramMap) {
	     return pBoardDAO.getPagedReviewsByProductId(paramMap);
	 }

	 @Override
	 public int getReviewCountByProductId(int productId) {
	     return pBoardDAO.getReviewCountByProductId(productId);
	 }
	 
	 @Override
	 public List<PReviewVO> getReviewsPaged(Map<String, Object> paramMap) {
	     return pBoardDAO.getReviewsPaged(paramMap);
	 }

	 @Override
	 public List<ReviewImageVO> getImagesByReviewId(Long reviewId) {
	     return pBoardDAO.getImagesByReviewId(reviewId);
	 }

	 @Override
	 public int getTotalReviewCountByUser(Map<String, Object> paramMap) {
	     return pBoardDAO.getTotalReviewCountByUser(paramMap);
	 }
	 
	 @Override
	 public ProductBoardVO getProductById(int productId) {
	     // 1. 상품 기본 정보 + 대표 이미지 조회
	     ProductBoardVO product = pBoardDAO.getProductById(productId);

	     if (product == null) {
	         return null;
	     }

	     // 2. 추가 이미지 리스트 조회
	     List<ProductImageVO> imageList = pBoardDAO.getImagesByProductId(productId);

	     // 3. VO에 이미지 리스트 주입
	     product.setImages(imageList);

	     return product;
	 }

	 
	 @Override
	 public List<ProductBoardVO> getSaleProducts() {
	     return pBoardDAO.getSaleProducts();
	 }

	 @Override
	 public List<ProductBoardVO> getGroupProducts() {
	     return pBoardDAO.getGroupProducts();
	 }

	 @Override
	 public List<ProductBoardVO> getPromotionProducts() {
	     return pBoardDAO.getPromotionProducts();
	 }
	 
	 @Override
	 public List<ProductBoardVO> searchProducts(String keyword) {
	     return pBoardDAO.searchProducts(keyword);
	 }
	 
	 @Override
	 public void placeGuestOrder(OrderVO order) {
	     // 결제 수단에 따른 BANK_STATUS
		 if ("카드".equalsIgnoreCase(order.getPaymentMethod())) {
			    order.setBankStatus("입금");  // DB 체크 제약조건에 맞춰서 정확히
			} else {
			    order.setBankStatus("입금하지_않음");  // 또는 "입금전"이 DB와 맞는지 확인
			}

	     order.setOrderStatus("주문확인");
	     order.setIsCanceled("N");

	     pBoardDAO.insertGuestOrder(order);
	 }
	 
	 @Override
	 public List<OrderVO> getOrderInfoByCustomerId(String customerId) {
	     return pBoardDAO.getOrderInfoByCustomerId(customerId);
	 }

	 @Override
	 public ProductBoardVO getProductById(String productId) {
	     return pBoardDAO.getProductImageById(productId);
	 }
	 
	 @Override
	 public List<String> getDetailImagesByProductId(int productId) {
	     List<ProductDetailImageVO> list = pBoardDAO.getDetailImagesByProductId(productId);
	     List<String> base64List = new ArrayList<>();
	     for(ProductDetailImageVO img : list) {
	         if(img.getImage() != null && img.getImage().length > 0) {
	             String base64 = Base64.getEncoder().encodeToString(img.getImage());
	             base64List.add("data:" + (img.getImageType() != null ? img.getImageType() : "image/jpeg") + ";base64," + base64);
	         }
	     }
	     return base64List;
	 }
	 
	 @Override
	 public void cancelOrder(Long orderId, String isCanceled) {
	     pBoardDAO.updateOrderStatus(orderId, isCanceled);
	 }
	 
	 @Override
	 public void createReturnRequest(ReturnRequest request, List<ReturnImage> images) {
	     // RETURN_ID 생성 (예: 시퀀스 또는 UUID)
	     Long returnId = pBoardDAO.getNextReturnId();
	     request.setReturnId(returnId);

	     pBoardDAO.insertReturnRequest(request);

	     for (ReturnImage image : images) {
	         image.setReturnId(returnId);
	         pBoardDAO.insertReturnImage(image);
	     }
	 }
	 
	 @Override
	 public List<ReturnRequest> getReturnRequestsByOrderId(Long orderId) {
	     return pBoardDAO.selectReturnRequestsByOrderId(orderId);
	 }

	 @Override
	 public List<ReturnImage> getReturnImagesByReturnId(Long returnId) {
	     return pBoardDAO.selectReturnImages(returnId);
	 }
	 
	 @Transactional
	 public void insertReviewWithImages(PReviewVO review, List<ReviewImageVO> images) {
	     pBoardDAO.insertReview(review);  // reviewId가 VO에 세팅됨
	     for (ReviewImageVO img : images) {
	         img.setReviewId(review.getReviewId()); // 부모 키 지정
	         pBoardDAO.insertReviewImage(img);
	     }
	 }
	 
	 @Transactional
	 @Override
	 public boolean deleteReview(Long reviewId, String userId, String userGrade) {
	     PReviewVO review = pBoardDAO.getReviewById(reviewId);
	     if (review == null) return false;

	     boolean isOwner = userId.equals(review.getUserId());
	     boolean isAuthorized = userGrade != null && List.of("운영자", "사원", "매니저").contains(userGrade);

	     if (isOwner || isAuthorized) {
	         pBoardDAO.deleteReviewImages(reviewId);
	         pBoardDAO.deleteReview(reviewId);
	         return true;
	     }

	     return false;
	 }

	 @Override
	 public Long getProductIdByReviewId(Long reviewId) {
	     return pBoardDAO.getProductIdByReviewId(reviewId);
	 }

	 @Override
	 public PReviewVO getReviewById(Long reviewId) {
	     return pBoardDAO.getReviewById(reviewId);
	 }
	 
	 @Override
	 public void updateReview(Long reviewId, Long productId, String reviewComment, MultipartFile[] newImages) {
	     // 리뷰 내용 수정
	     pBoardDAO.updateReviewComment(reviewId, reviewComment);

	     // 기존 이미지 삭제
	     pBoardDAO.deleteReviewImages(reviewId);

	     // 새 이미지 저장
	     if (newImages != null) {
	         for (MultipartFile file : newImages) {
	             if (!file.isEmpty()) {
	                 try {
	                     ReviewImageVO image = new ReviewImageVO();

	                        // 자료형 맞춰서 수정
	                     image.setReviewId(reviewId.intValue()); // Long → int
	                     image.setImageName(file.getOriginalFilename());
	                     image.setImageData(file.getBytes());

	                     // java.util.Date로 설정
	                     image.setUploadDate(new Date());

	                     pBoardDAO.insertReviewImage(image);
	                 } catch (IOException e) {
	                     throw new RuntimeException("이미지 업로드 실패", e);
	                 }
	             }
	         }
	     }

	     // 수정 시간 업데이트
	     pBoardDAO.updateReviewTimestamp(reviewId);
	 }
}
