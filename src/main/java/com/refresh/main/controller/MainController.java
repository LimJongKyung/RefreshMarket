package com.refresh.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

import com.refresh.board.service.PBoardService;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.member.service.MemberService;
import com.refresh.menu.service.MenuService;

@Controller
public class MainController {
	
	@Autowired
    private final MenuService menuService;
	private final PBoardService productService;
	private final MemberService memberService;

    public MainController(MenuService menuService, PBoardService productService, MemberService memberService) {
        this.menuService = menuService;
        this.productService = productService;
        this.memberService = memberService;
    }
	
    // 일반 메인
	@GetMapping("/")
	public String main(Model model) {		
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        List<ProductBoardVO> saleProducts = productService.getSaleProducts();
        List<ProductBoardVO> groupProducts = productService.getGroupProducts();
        List<ProductBoardVO> promotionProducts = productService.getPromotionProducts();

        model.addAttribute("saleProducts", saleProducts);
        model.addAttribute("groupProducts", groupProducts);
        model.addAttribute("promotionProducts", promotionProducts);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
		return "refresh/main";
	}
	
	// 로그인 메인
    @GetMapping("/home")
    public String forCheckingId(Model model, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        List<ProductBoardVO> saleProducts = productService.getSaleProducts();
        List<ProductBoardVO> groupProducts = productService.getGroupProducts();
        List<ProductBoardVO> promotionProducts = productService.getPromotionProducts();
        String username = null;

        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }

        model.addAttribute("saleProducts", saleProducts);
        model.addAttribute("groupProducts", groupProducts);
        model.addAttribute("promotionProducts", promotionProducts);
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("username", username);
        return "refresh/mainlogin"; // 해당하는 HTML 파일 이름 반환 (예: home.html)
    }
	
	@GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("id") int id) {
        ProductBoardVO product = productService.getProductById(id);
        byte[] imageData = product.getImage();
        String imageType = product.getImageType();

        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }

        if (imageType == null || imageType.isEmpty()) {
            imageType = "image/jpeg"; // 기본값 지정 (필요하면 변경)
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageType))
                .body(imageData);
    }
}
