package com.refresh.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;

import com.refresh.board.dao.PBoardDAO;
import com.refresh.board.service.PBoardService;
import com.refresh.board.vo.CartItem;
import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.board.vo.ProductImageVO;
import com.refresh.board.vo.ReviewImageVO;
import com.refresh.member.service.MemberService;
import com.refresh.member.vo.MemberBenefit;
import com.refresh.member.vo.MemberVO;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class ProductBoardController {

    @Autowired
    private final PBoardService productService;
    private final PBoardDAO pBoardDAO;
    private final MenuService menuService;
    private final MemberService memberService;

    public ProductBoardController(MenuService menuService, MemberService memberService,
    		PBoardService productService, PBoardDAO pBoardDAO) {
        this.menuService = menuService;
        this.memberService = memberService;
        this.productService = productService;
        this.pBoardDAO = pBoardDAO;
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
    
    // 로그인 전 디테일
    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable("id") int productId,
					            @RequestParam(required = false) String category,
					            @RequestParam(defaultValue = "1") int page,
					            Model model) {

        ProductBoardVO product = productService.getProductById(productId);
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", productId);
        paramMap.put("offset", offset);
        paramMap.put("limit", pageSize);

        List<PReviewVO> reviews = productService.getPagedReviewsByProductId(paramMap);
        int totalCount = productService.getReviewCountByProductId(productId);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 🔥 리뷰 이미지 리스트 세팅
        for (PReviewVO review : reviews) {
            List<ReviewImageVO> images = productService.getImagesByReviewId((long) review.getReviewId());
            review.setImageList(images);
        }

        // 🔥 추가 이미지 리스트
        List<ProductImageVO> additionalImages = pBoardDAO.getImagesByProductId(productId);
        model.addAttribute("additionalImages", additionalImages);

        // 🔥 상세 이미지 리스트
        List<String> detailImages = productService.getDetailImagesByProductId(productId);
        model.addAttribute("detailImages", detailImages);

        // 🔥 상세 옵션 리스트
        if (product.getDetailOption() != null) {
            List<String> options = Arrays.asList(product.getDetailOption().split(","));
            model.addAttribute("optionList", options);
        } else {
            model.addAttribute("optionList", null);
        }

        // 🔥 상세 옵션 가격 리스트
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
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("category", category);

        return "refresh/detail";
    }

    @GetMapping("/detailL/{id}")
    public String productDetailLogin(@PathVariable("id") int productId,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(defaultValue = "1") int page,
                                     Model model, HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        String username = userId != null ? memberService.getUserByName(userId) : null;
        String userGrade = null;

        ProductBoardVO product = productService.getProductById(productId);
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", productId);
        paramMap.put("offset", offset);
        paramMap.put("limit", pageSize);

        List<PReviewVO> reviews = productService.getPagedReviewsByProductId(paramMap);
        int totalCount = productService.getReviewCountByProductId(productId);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 🔥 리뷰 이미지 리스트 세팅
        for (PReviewVO review : reviews) {
            List<ReviewImageVO> images = productService.getImagesByReviewId((long) review.getReviewId());
            review.setImageList(images);
        }

        if (userId != null) {
            MemberVO member = memberService.getUserById(userId);
            if (member != null) {
                userGrade = member.getGrade(); // 또는 getUserGrade()
            }
        }
        
        // 🔥 추가 이미지 리스트
        List<ProductImageVO> additionalImages = pBoardDAO.getImagesByProductId(productId);
        model.addAttribute("additionalImages", additionalImages);

        // 🔥 상세 이미지 리스트
        List<String> detailImages = productService.getDetailImagesByProductId(productId);
        model.addAttribute("detailImages", detailImages);

        // 🔥 상세 옵션 리스트
        if (product.getDetailOption() != null) {
            List<String> options = Arrays.asList(product.getDetailOption().split(","));
            model.addAttribute("optionList", options);
        } else {
            model.addAttribute("optionList", null);
        }

        // 🔥 상세 옵션 가격 리스트
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

        model.addAttribute("userGrade", userGrade);
        model.addAttribute("username", username);
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("category", category);

        return "refresh/detailLogin";
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

    // 메인 추가 이미지
    @GetMapping("/detail-image/{productId}/{index}")
    public ResponseEntity<byte[]> getDetailImageByIndex(@PathVariable("productId") int productId,
                                                        @PathVariable("index") int index) {
        // 🔥 전체 이미지 말고, 해당 productId의 이미지 리스트만 가져옴
        List<ProductImageVO> imageList = pBoardDAO.getImagesByProductId(productId);

        if (imageList == null || imageList.size() <= index) {
            return ResponseEntity.notFound().build();
        }

        ProductImageVO imageVO = imageList.get(index);

        if (imageVO == null || imageVO.getImage() == null || imageVO.getImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String imageType = imageVO.getImageType();
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
                .body(imageVO.getImage());
    }
  
    // 🔥 디테일 이미지 렌더링
    @GetMapping("/detail-images/{productId}/{index}")
    public ResponseEntity<byte[]> getDetailImage(@PathVariable("productId") int productId,
                                                 @PathVariable("index") int index) {
        // ProductBoardVO에서 이미지 리스트 가져오기
        ProductBoardVO product = productService.getProductById(productId);
        if (product == null || product.getImages() == null || product.getImages().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ProductImageVO> imageList = product.getImages(); // ProductBoardVO에 getImages() 존재

        if (index < 0 || index >= imageList.size()) {
            return ResponseEntity.notFound().build();
        }

        ProductImageVO imageVO = imageList.get(index);
        if (imageVO == null || imageVO.getImage() == null || imageVO.getImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String imageType = imageVO.getImageType();
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
                .body(imageVO.getImage());
    }

    @GetMapping("/review-image/{reviewId}/{index}")
    public ResponseEntity<byte[]> getReviewImage(@PathVariable("reviewId") Long reviewId,
                                                 @PathVariable("index") int index) {
        List<ReviewImageVO> imageList = productService.getImagesByReviewId(reviewId);
        if (imageList == null || imageList.isEmpty() || index < 0 || index >= imageList.size()) {
            return ResponseEntity.notFound().build();
        }

        ReviewImageVO imageVO = imageList.get(index);
        if (imageVO == null || imageVO.getImageData() == null || imageVO.getImageData().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String name = imageVO.getImageName();
        String imageType = "image/jpeg";
        if (name != null && name.contains(".")) {
            String ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
            switch (ext) {
                case "png": imageType = "image/png"; break;
                case "gif": imageType = "image/gif"; break;
                case "jpg": case "jpeg": imageType = "image/jpeg"; break;
                case "webp": imageType = "image/webp"; break;
            }
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageType))
                .cacheControl(CacheControl.noCache())
                .body(imageVO.getImageData());
    }
    
    // 로그인전 상품 검색
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
    
    // 로그인후 상품 검색
    @GetMapping("/searchL")
    public String searchProductsLogin(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
    	String userId = (String) session.getAttribute("userId");
        List<ProductBoardVO> products = productService.searchProducts(keyword);
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        String username = null;
        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }
        model.addAttribute("username", username);
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "refresh/psearchlogin";  // 검색 결과를 보여줄 뷰 이름
    }
    
    // 비회원 구매
    @GetMapping("/purchase")
    public String purchase(Model model) {
    	List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
    	return "refresh/purchase";
    }
    
    @GetMapping("/purchaseL")
    public String purchaselogin(Model model, HttpSession session) {
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        String userId = (String) session.getAttribute("userId");
        String username = null;
        List<MemberBenefit> benefits = new ArrayList<>();

        if (userId != null) {
            username = memberService.getUserByName(userId);
            benefits = memberService.getBenefits(userId); // 혜택 조회
        }
        if (userId != null) {
            username = memberService.getUserByName(userId);
            benefits = memberService.getBenefits(userId);
            MemberVO memberInfo = memberService.getUserById(userId); // 포인트 포함된 객체
            model.addAttribute("memberInfo", memberInfo);
        }

        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        model.addAttribute("benefits", benefits); // 혜택 추가
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);

        return "refresh/purchaselogin";
    }
    
    @GetMapping("/user-info")
    public ResponseEntity<Map<String, String>> getUserInfo(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MemberVO member = memberService.getUserById(userId);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Map<String, String> info = new HashMap<>();
        info.put("customerId", member.getId());  // 추가
        info.put("name", member.getName());
        info.put("email", member.getEmail());
        info.put("phone", member.getPhoneNumber());
        info.put("address", member.getAddress());

        return ResponseEntity.ok(info);
    }

    @PostMapping("/order")
    @ResponseBody
    public String guestOrderSubmit(
            @RequestParam(required = false) String customerId,
            @RequestParam String customerName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String shippingAddress,
            @RequestParam String deliveryRequest,
            @RequestParam String paymentMethod,
            @RequestParam String cartData,
            @RequestParam(required = false) String usedBenefits,
            @RequestParam(required = false, defaultValue = "0") int usedPoint) {

        // 비회원 주문 시 customerId를 null로 설정
        if (customerId == null || customerId.trim().isEmpty()) {
            customerId = null;
        }

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

        if (usedPoint > 0 && customerId != null) {
            MemberVO member = memberService.getUserById(customerId); // 기존 메서드 활용
            int currentPoint = member.getPoint(); // 포인트 조회

            if (usedPoint <= currentPoint) {
                memberService.usePoint(customerId, usedPoint); // 포인트 차감
            } else {
                System.err.println("포인트 차감 실패: 보유 포인트 초과");
            }
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

        int totalQuantity = cart.stream().mapToInt(CartItem::getQuantity).sum();
        int sumTotalPrice = cart.stream().mapToInt(item -> item.getQuantity() * item.getPrice()).sum();

        OrderVO order = new OrderVO();
        order.setCustomerId(customerId);
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setPhoneNumber(phoneNumber);
        order.setShippingAddress(shippingAddress);
        order.setDeliveryRequest(deliveryRequest);
        order.setPaymentMethod(paymentMethod);
        order.setProductId(productId);
        order.setProductQuantities(quantities);
        order.setDetailOption(detailOption);
        order.setQuantity(totalQuantity);
        order.setTotalPrice(sumTotalPrice);

        productService.placeGuestOrder(order);

        // ✅ 사용된 쿠폰 삭제 처리
        if (usedBenefits != null && customerId != null) {
            try {
                List<String> benefitsToRemove = mapper.readValue(usedBenefits, new TypeReference<List<String>>() {});
                memberService.deleteBenefits(customerId, benefitsToRemove);
            } catch (Exception e) {
                System.err.println("쿠폰 삭제 실패: " + e.getMessage());
            }
        }

        return "<script>alert('구매가 완료되었습니다! 감사합니다!'); window.location='/home';</script>";
    }
    
    @GetMapping("/createreview/{id}")
    public String createReview (@PathVariable("id") int productId,
		            @RequestParam(required = false) String category,
		            @RequestParam(defaultValue = "1") int page,
		            Model model, HttpSession session) {
		
		String userId = (String) session.getAttribute("userId");
		String username = userId != null ? memberService.getUserByName(userId) : null;
		
		ProductBoardVO product = productService.getProductById(productId);
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
		List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
		
		int pageSize = 5;
		int offset = (page - 1) * pageSize;
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("productId", productId);
		paramMap.put("offset", offset);
		paramMap.put("limit", pageSize);
		
		List<PReviewVO> reviews = productService.getPagedReviewsByProductId(paramMap);
		int totalCount = productService.getReviewCountByProductId(productId);
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		
		// 🔥 리뷰 이미지 리스트 세팅
		for (PReviewVO review : reviews) {
		    List<ReviewImageVO> images = productService.getImagesByReviewId((long) review.getReviewId());
		    review.setImageList(images); 
		    System.out.println("reviewId=" + review.getReviewId() + ", imageCount=" + (images != null ? images.size() : 0));
		}
		
		// 🔥 추가 이미지 리스트
		List<ProductImageVO> additionalImages = pBoardDAO.getImagesByProductId(productId);
		model.addAttribute("additionalImages", additionalImages);
		
		// 🔥 상세 이미지 리스트
		List<String> detailImages = productService.getDetailImagesByProductId(productId);
		model.addAttribute("detailImages", detailImages);
		
		// 🔥 상세 옵션 리스트
		if (product.getDetailOption() != null) {
		List<String> options = Arrays.asList(product.getDetailOption().split(","));
		model.addAttribute("optionList", options);
		} else {
		model.addAttribute("optionList", null);
		}
		
		// 🔥 상세 옵션 가격 리스트
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
		
		model.addAttribute("username", username);
		model.addAttribute("product", product);
		model.addAttribute("reviews", reviews);
		model.addAttribute("sidebarMenus", sidebarMenus);
		model.addAttribute("headerMenus", headerMenus);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("category", category);
		
    	return "refresh/createreview";
    }
    
    @PostMapping("/createreview")
    public String createReview(@ModelAttribute PReviewVO review,
                               @RequestParam("reviewImages") MultipartFile[] files,
                               HttpSession session, Model model) throws IOException {
        String userId = (String) session.getAttribute("userId");
        String userName = userId != null ? memberService.getUserByName(userId) : null;

        review.setUserId(userId);
        review.setUserName(userName);

        // MultipartFile -> ReviewImageVO 리스트 변환
        List<ReviewImageVO> imageList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                ReviewImageVO img = new ReviewImageVO();
                img.setImageName(file.getOriginalFilename());
                img.setImageData(file.getBytes());
                img.setUploadDate(new Timestamp(System.currentTimeMillis()));
                imageList.add(img);
            }
        }

        // 서비스 호출 (서비스에서 reviewId 세팅 후 이미지 reviewId도 세팅됨)
        productService.insertReviewWithImages(review, imageList);
        model.addAttribute("userName", userName);

        return "redirect:/products/detailL/" + review.getProductId();
    }
    
    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam Long reviewId,
                               @RequestParam int productId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        PReviewVO review = productService.getReviewById(reviewId);
        if (review == null) {
            redirectAttributes.addFlashAttribute("error", "리뷰를 찾을 수 없습니다.");
            return "redirect:/products/detailL/" + productId;
        }

        MemberVO member = memberService.getUserById(userId);
        String grade = member != null ? member.getGrade() : null;

        boolean isOwner = userId.equals(review.getUserId());
        boolean isAuthorized = grade != null && List.of("운영자", "사원", "매니저").contains(grade);

        if (isOwner || isAuthorized) {
            productService.deleteReview(reviewId, userId, grade);
            redirectAttributes.addFlashAttribute("alertMessage", "삭제되었습니다!");
        } else {
            redirectAttributes.addFlashAttribute("alertMessage", "삭제 권한이 없습니다.");
        }

        return "redirect:/products/detailL/" + productId;
    }
    
    @PostMapping("/review/update")
    public String updateReview(@RequestParam("reviewId") Long reviewId,
                               @RequestParam("productId") Long productId,
                               @RequestParam("reviewComment") String reviewComment,
                               @RequestParam(value = "newImages", required = false) MultipartFile[] newImages,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        // 1. 로그인된 사용자 ID 확인
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        // 2. 리뷰 정보 조회
        PReviewVO review = productService.getReviewById(reviewId);
        if (review == null) {
            redirectAttributes.addFlashAttribute("error", "리뷰를 찾을 수 없습니다.");
            return "redirect:/products/detailL/" + productId;
        }

        // 3. 회원 등급 조회
        MemberVO member = memberService.getUserById(userId);
        String grade = member != null ? member.getGrade() : null;

        // 4. 권한 체크
        boolean isOwner = userId.equals(review.getUserId());
        boolean isAuthorized = grade != null && List.of("운영자", "사원", "매니저").contains(grade);

        if (isOwner || isAuthorized) {
            productService.updateReview(reviewId, productId, reviewComment, newImages);
            redirectAttributes.addFlashAttribute("alertMessage", "수정되었습니다!");
        } else {
            redirectAttributes.addFlashAttribute("alertMessage", "자기자신의 리뷰만 수정할 수 있습니다!");
        }

        return "redirect:/products/detailL/" + productId;
    }

}
