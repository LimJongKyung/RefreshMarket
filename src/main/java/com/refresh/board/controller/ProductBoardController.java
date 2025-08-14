package com.refresh.board.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.refresh.board.service.PBoardService;
import com.refresh.board.vo.CartItem;
import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.member.service.MemberService;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class ProductBoardController {

    @Autowired
    private PBoardService productService;
    private final MenuService menuService;
    private final MemberService memberService;

    public ProductBoardController(MenuService menuService, MemberService memberService) {
        this.menuService = menuService;
        this.memberService = memberService;
    }
    
    // 로그인 전 리스트
    @GetMapping
    public String productBoard(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model) {

        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        List<MenuVO> menus = menuService.getAllMenus();
        model.addAttribute("menus", menus);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);

        if (category != null && !category.isEmpty()) {
            int pageSize = 40; // 5 x 8 카드 기준
            int offset = (page - 1) * pageSize;

            List<ProductBoardVO> products = menuService.getProductsByCategoryPaged(category, offset, pageSize);
            int totalCount = menuService.countProductsByCategory(category);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            model.addAttribute("products", products);
            model.addAttribute("selectedCategory", category);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
        } else {
            model.addAttribute("products", Collections.emptyList());
        }

        return "refresh/productboard";
    }
    
    // 로그인 후 리스트
    @GetMapping("/login")
    public String productBoardLogin(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model, HttpSession session) {
    	String userId = (String) session.getAttribute("userId");
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        List<MenuVO> menus = menuService.getAllMenus();
        model.addAttribute("menus", menus);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        String username = null;
        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }
        model.addAttribute("username", username);
        
        if (category != null && !category.isEmpty()) {
            int pageSize = 40; // 5 x 8 카드 기준
            int offset = (page - 1) * pageSize;

            List<ProductBoardVO> products = menuService.getProductsByCategoryPaged(category, offset, pageSize);
            int totalCount = menuService.countProductsByCategory(category);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            model.addAttribute("products", products);
            model.addAttribute("selectedCategory", category);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
        } else {
            model.addAttribute("products", Collections.emptyList());
        }

        return "refresh/productboardlogin";
    }
    
    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable("id") int productId, Model model) {
        ProductBoardVO product = productService.getProductById(productId);
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        List<PReviewVO> reviews = productService.getReviewsByProductId(productId);

        // 옵션 리스트
        if (product.getDetailOption() != null) {
            List<String> options = Arrays.asList(product.getDetailOption().split(","));
            model.addAttribute("optionList", options);
        } else {
            model.addAttribute("optionList", null);
        }

        // 옵션별 추가 가격 리스트 (문자열 → 숫자 리스트 변환)
        if (product.getDetailOptionPrice() != null) {
            String[] priceStrArr = product.getDetailOptionPrice().split(",");
            List<Integer> optionPriceList = new ArrayList<>();
            for (String p : priceStrArr) {
                try {
                    optionPriceList.add(Integer.parseInt(p.trim()));
                } catch (NumberFormatException e) {
                    optionPriceList.add(0);
                }
            }
            model.addAttribute("optionPriceList", optionPriceList);
        } else {
            model.addAttribute("optionPriceList", null);
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);

        return "refresh/detail";
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
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<ProductBoardVO> products = productService.searchProducts(keyword);
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "refresh/psearch";  // 검색 결과를 보여줄 뷰 이름
    }
    
    @GetMapping("/purchase")
    public String purchase(Model model) {
    	List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
    	return "refresh/purchase";
    }
    
    @PostMapping("/order")
    @ResponseBody
    public String guestOrderSubmit(@RequestParam String customerName,
                                   @RequestParam String email,
                                   @RequestParam String phoneNumber,
                                   @RequestParam String shippingAddress,
                                   @RequestParam String deliveryRequest,
                                   @RequestParam String paymentMethod,
                                   @RequestParam String cartData) {

        ObjectMapper mapper = new ObjectMapper();
        List<CartItem> cart = null;
        try {
            cart = mapper.readValue(cartData, new TypeReference<List<CartItem>>() {});
        } catch (Exception e) {
            return "<script>alert('장바구니 데이터 처리 오류'); window.location='/midnav/cart';</script>";
        }

        if (cart == null || cart.isEmpty()) {
            return "<script>alert('장바구니가 비어있습니다.'); window.location='/midnav/cart';</script>";
        }

        String productId = cart.stream()
                .map(item -> String.valueOf(item.getProductId()))
                .collect(Collectors.joining(","));

        String quantities = cart.stream()
                .map(item -> String.valueOf(item.getQuantity()))
                .collect(Collectors.joining(","));
        
        String detailOption = cart.stream()
        		.map(item -> (item.getOption() != null && !item.getOption().trim().isEmpty()) ? item.getOption() : "없음")
        		.collect(Collectors.joining(","));

        int totalQuantity = cart.stream()
                                .mapToInt(CartItem::getQuantity)
                                .sum();

        int sumTotalPrice = cart.stream()
                                .mapToInt(item -> item.getQuantity() * item.getPrice())
                                .sum();

        OrderVO order = new OrderVO();
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setPhoneNumber(phoneNumber);
        order.setShippingAddress(shippingAddress);
        order.setDeliveryRequest(deliveryRequest);
        order.setPaymentMethod(paymentMethod);

        order.setProductId(productId);
        order.setProductQuantities(quantities);
        order.setDetailOption(detailOption);  // 추가한 부분
        order.setQuantity(totalQuantity);
        order.setTotalPrice(sumTotalPrice);

        productService.placeGuestOrder(order);

        return "<script>alert('구매가 완료되었습니다! 감사합니다!'); window.location='/';</script>";
    }
}
