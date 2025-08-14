package com.refresh.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.refresh.board.dao.PBoardDAO;
import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

@Service
public class PBoardServiceImpl implements PBoardService{
	
	 @Autowired
	 private PBoardDAO pBoardDAO;
	 // 계절 상품
	 @Override
	 public List<ProductBoardVO> productboard() {
	     return pBoardDAO.productboard();
	 }
	
	 @Override
	    public List<PReviewVO> getReviewsByProductId(int productId) {
	        return pBoardDAO.getReviewsByProductId(productId);
	    }
	 
	 @Override
	 public ProductBoardVO getProductById(int productId) {
	     return pBoardDAO.getProductById(productId);
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
	     pBoardDAO.insertGuestOrder(order);
	 }
}
