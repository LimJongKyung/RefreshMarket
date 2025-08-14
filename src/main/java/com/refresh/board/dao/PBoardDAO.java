package com.refresh.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

@Mapper
public interface PBoardDAO {
	// 상품 리스트
	List<ProductBoardVO> productboard();

	// 리뷰
	List<PReviewVO> getReviewsByProductId(int productId);

    ProductBoardVO getProductById(int productId);
    
    List<ProductBoardVO> getSaleProducts();
    List<ProductBoardVO> getGroupProducts();
    List<ProductBoardVO> getPromotionProducts();
    
    List<ProductBoardVO> searchProducts(String keyword);
    
    void insertGuestOrder(OrderVO order);
}
