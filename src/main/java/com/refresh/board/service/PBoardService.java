package com.refresh.board.service;

import java.util.List;

import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

public interface PBoardService {
	// 계절 상품
	List<ProductBoardVO> seasonProducts();
	// 지역 상품
	List<ProductBoardVO> localProducts();
	// 세일 상품
	List<ProductBoardVO> saleProducts();
	// 공동 구매 상품
	List<ProductBoardVO> groupPurchaseProducts();
	// 중소기업 상품
	List<ProductBoardVO> smallBusinessProducts();
	// 상품 디테일
	ProductBoardVO findById(int productId);
	// 상품에 대한 리뷰 목록 조회
    List<PReviewVO> getReviewsByProductId(int productId);
}
