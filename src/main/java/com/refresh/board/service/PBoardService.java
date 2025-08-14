package com.refresh.board.service;

import java.util.List;

import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

public interface PBoardService {
	// 계절 상품
	List<ProductBoardVO> productboard();

	// 상품에 대한 리뷰 목록 조회
	List<PReviewVO> getReviewsByProductId(int productId);
    
    ProductBoardVO getProductById(int productId); // 상세  + 이미지용
    
    List<ProductBoardVO> getSaleProducts();
    List<ProductBoardVO> getGroupProducts();
    List<ProductBoardVO> getPromotionProducts();

    public List<ProductBoardVO> searchProducts(String keyword);
    
    void placeGuestOrder(OrderVO order);
}
