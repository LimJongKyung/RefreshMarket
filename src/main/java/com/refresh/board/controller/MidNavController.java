package com.refresh.board.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.refresh.board.service.PBoardService;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.board.vo.TrackingInfo;
import com.refresh.member.service.MemberService;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/midnav")
public class MidNavController {
	
	private final MenuService menuService;
	private final MemberService memberService;
	private final PBoardService productService;
	
	@Autowired
	public MidNavController (MenuService menuService, MemberService memberService,
			PBoardService productService) {
		this.menuService = menuService;
		this.memberService = memberService;
		this.productService = productService;
	}

	// 로그인 전 배송조회 서비스
	@GetMapping("/tracking")
	public String showTrackingPage(Model model) {
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        return "refresh/midnav/Tracking"; // templates/refresh/midnav/tracking.html
    }
	
	// 로그인 전 배송조회 서비스
	@GetMapping("/trackingL")
	public String showTrackingPagelogin(Model model, HttpSession session) {
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
	    List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
	    String userId = (String) session.getAttribute("userId");

	    String username = null;
        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }
        model.addAttribute("username", username);
	    model.addAttribute("sidebarMenus", sidebarMenus);
	    model.addAttribute("headerMenus", headerMenus);
	    return "refresh/midnav/Trackinglogin"; // templates/refresh/midnav/tracking.html
	}
	
	@GetMapping("/{invoiceNumber}")
    public ResponseEntity<TrackingInfo> getTrackingInfo(@PathVariable String invoiceNumber) {
        // 실제로는 택배사 API 호출 또는 DB 조회
        TrackingInfo info = new TrackingInfo();
        info.setStatus("배송중");
        info.setLocation("서울 중구");
        info.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(info);
    }
	
	// 장바구니 서비스
	@GetMapping("/cart")
	public String showCart(Model model, HttpSession session) {
		// 초기 데이터 설정 (로컬 스토리지와 별도로 백엔드 데이터 사용 가능)
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        //String userId = (String) session.getAttribute("userId");
		//String name = (String) memberService.getUserByName(userId);
		// 초기 데이터 설정 (로컬 스토리지와 별도로 백엔드 데이터 사용 가능)		
		model.addAttribute("cartItems", new ArrayList<>());
		//model.addAttribute("username", name);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
		return "refresh/midnav/Cart";
	}
	
	// 장바구니 서비스
	@GetMapping("/cartL")
	public String showCartLogin(Model model, HttpSession session) {
		// 초기 데이터 설정 (로컬 스토리지와 별도로 백엔드 데이터 사용 가능)
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
	    List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
	    String userId = (String) session.getAttribute("userId");
		String name = (String) memberService.getUserByName(userId);
		// 초기 데이터 설정 (로컬 스토리지와 별도로 백엔드 데이터 사용 가능)		
		model.addAttribute("cartItems", new ArrayList<>());
		model.addAttribute("username", name);
	    model.addAttribute("sidebarMenus", sidebarMenus);
	    model.addAttribute("headerMenus", headerMenus);
	    return "refresh/midnav/Cartlogin";
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
	
	@GetMapping("/main-image/{productId}")
    public ResponseEntity<byte[]> getMainImage(@PathVariable("productId") int productId) {
        ProductBoardVO product = productService.getProductById(productId);

        if (product == null || product.getImage() == null || product.getImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String imageType = product.getImageType();
        MediaType mediaType;

        try {
            if (imageType == null || !imageType.startsWith("image/")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else {
                if (imageType.equalsIgnoreCase("image/jpg")) {
                    imageType = "image/jpeg";
                }
                mediaType = MediaType.parseMediaType(imageType);
            }
        } catch (InvalidMimeTypeException e) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(product.getImage());
    }
}
