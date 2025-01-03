package com.refresh.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.refresh.board.dao.PBoardDAO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;

@Service
public class PBoardServiceImpl implements PBoardService{
	
	 @Autowired
	 private PBoardDAO pBoardDAO;
	 // 계절 상품
	 @Override
	 public List<ProductBoardVO> seasonProducts() {
	     return pBoardDAO.seasonProducts();
	 }
	 // 지역 상품
	 @Override
	 public List<ProductBoardVO> localProducts() {
	     return pBoardDAO.localProducts();
	 }
	 // 세일 상품
	 @Override
	 public List<ProductBoardVO> saleProducts() {
		 return pBoardDAO.saleProducts();
	 }
	 // 공동 구매 상품
	 @Override
	 public List<ProductBoardVO> groupPurchaseProducts() {
		 return pBoardDAO.groupPurchaseProducts();
	 }
	 // 중소기업 상품
	 @Override
	 public List<ProductBoardVO> smallBusinessProducts() {
		 return pBoardDAO.smallBusinessProducts();
	 }
	 // 디테일
	 @Override
	 public ProductBoardVO findById(int productId) {
	     return pBoardDAO.findById(productId);
	 }
	 @Override
	 public List<PReviewVO> getReviewsByProductId(int productId) {
	     return pBoardDAO.selectReviewsByProductId(productId);
	 }
}
