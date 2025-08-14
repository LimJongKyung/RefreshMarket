package com.refresh.menu.service;

import java.util.List;

import com.refresh.board.vo.ProductBoardVO;
import com.refresh.menu.vo.MenuVO;

public interface MenuService {
	// 위치 기준으로 메뉴 리스트 조회
    List<MenuVO> getMenusByPosition(String position);

    List<MenuVO> getAllMenus();
    
    List<ProductBoardVO> getProductsByCategory(String category);
    
    // 카테고리 기준으로 제품 리스트 조회
    List<ProductBoardVO> getProductsByCategoryPaged(String category, int offset, int limit);
    int countProductsByCategory(String category);
}
