package com.refresh.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.refresh.menu.vo.MenuVO;
import com.refresh.menu.dao.MenuDAO;
import com.refresh.board.vo.ProductBoardVO;

@Service
public class MenuServiceImpl implements MenuService {

	private final MenuDAO menuDAO;

    @Autowired
    public MenuServiceImpl(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Override
    public List<MenuVO> getMenusByPosition(String position) {
        return menuDAO.selectMenusByPosition(position);
    }

    @Override
    public List<MenuVO> getAllMenus() {
        return menuDAO.selectAllMenus();
    }
    
    @Override
    public List<ProductBoardVO> getProductsByCategory(String category) {
        return menuDAO.selectProductsByCategory(category);
    }
    
    @Override
    public List<ProductBoardVO> getProductsByCategoryPaged(String category, int offset, int limit) {
        return menuDAO.selectProductsByCategoryPaged(category, offset, limit);
    }

    @Override
    public int countProductsByCategory(String category) {
        return menuDAO.countProductsByCategory(category);
    }
}
