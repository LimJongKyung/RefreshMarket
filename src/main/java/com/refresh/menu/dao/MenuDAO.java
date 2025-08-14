package com.refresh.menu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.refresh.board.vo.ProductBoardVO;
import com.refresh.menu.vo.MenuVO;

@Mapper
public interface MenuDAO {
	// 위치(position) 기준 메뉴 조회
    List<MenuVO> selectMenusByPosition(String position);

    List<MenuVO> selectAllMenus();
    // 카테고리명(category) 기준 제품 조회
    List<ProductBoardVO> selectProductsByCategory(String category);
    
    List<ProductBoardVO> selectProductsByCategoryPaged(@Param("category") String category,
            @Param("offset") int offset,
            @Param("limit") int limit);

int countProductsByCategory(@Param("category") String category);

}
