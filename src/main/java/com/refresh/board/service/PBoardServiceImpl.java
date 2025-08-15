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
}
