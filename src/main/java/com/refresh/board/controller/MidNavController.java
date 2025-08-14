package com.refresh.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/midnav")
public class MidNavController {
	
	private final MenuService menuService;
	
	@Autowired
	public MidNavController (MenuService menuService) {
		this.menuService = menuService;
	}

	// 배송조회 서비스
	@GetMapping("/tracking")
	public String showTrackingPage(Model model) {
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        return "refresh/midnav/Tracking"; // templates/refresh/midnav/tracking.html
    }
	
	// 장바구니 서비스
	@GetMapping("/cart")
	public String showCart(Model model) {
		// 초기 데이터 설정 (로컬 스토리지와 별도로 백엔드 데이터 사용 가능)
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
		return "refresh/midnav/Cart";
	}
	
	@GetMapping("/favorites")
	@ResponseBody
	public String showFavorites(HttpSession session) {
	    // 로그인 여부 확인 (세션에 로그인 정보가 있을 경우
	    if (session.getAttribute("user") == null) {
	        return "<script>alert('로그인 후 이용 가능한 서비스입니다!'); window.location='/member/login';</script>";
	    }

	    return "favorites"; // 로그인 후 보여줄 페이지
	}
}
