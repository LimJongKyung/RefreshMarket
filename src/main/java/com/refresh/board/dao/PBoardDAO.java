package com.refresh.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

@Mapper
public interface PBoardDAO {
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
    List<PReviewVO> selectReviewsByProductId(int productId);
}
