package com.refresh.login.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.refresh.board.service.PBoardService;
import com.refresh.board.service.QBoardService;
import com.refresh.board.vo.OrderVO;
import com.refresh.board.vo.PReviewVO;
import com.refresh.board.vo.ProductBoardVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.board.vo.ReturnImage;
import com.refresh.board.vo.ReturnRequest;
import com.refresh.board.vo.ReviewImageVO;
import com.refresh.login.service.InfoService;
import com.refresh.member.service.MemberService;
import com.refresh.member.vo.MemberVO;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	private final InfoService infoService;
	private final MemberService memberService;
	private final MenuService menuService;
	private final PBoardService pBoardService;
	private final QBoardService qBoardService;
	
	public LoginController(InfoService infoService, MemberService memberService
			, MenuService menuService, PBoardService pBoardService,
			QBoardService qBoardService) {
		this.infoService = infoService;
		this.memberService = memberService;
		this.menuService = menuService;
		this.pBoardService = pBoardService;
		this.qBoardService = qBoardService;
	}
	
	@GetMapping("/information")
    public String getAllInfo(HttpSession session, Model model,
                             @RequestParam(value = "orderPage", defaultValue = "1") int orderPage,
                             @RequestParam(value = "orderSize", defaultValue = "5") int orderSize) {

        // 메뉴 정보
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        // 로그인 사용자
        String loginMember = (String) session.getAttribute("userId");
        String username = null;
        if (loginMember != null) {
            username = memberService.getUserByName(loginMember);
        }

        // 회원 정보
        MemberVO memberInfo = infoService.getAllInfo(loginMember);

        // 주문 정보 전체 가져오기
        List<OrderVO> allOrders = pBoardService.getOrderInfoByCustomerId(loginMember);

        // 주문 목록 페이징 처리
        int totalOrders = allOrders.size();
        int totalOrderPages = (int) Math.ceil((double) totalOrders / orderSize);
        int fromIndex = (orderPage - 1) * orderSize;
        int toIndex = Math.min(fromIndex + orderSize, totalOrders);
        List<OrderVO> pagedOrders = allOrders.subList(fromIndex, toIndex);

        // 주문별 상품 상세 매핑 (key: orderId)
        Map<Long, List<Map<String, Object>>> orderDetailsMap = new HashMap<>();
        Map<String, Boolean> productReturnStatusMap = new HashMap<>();

        for (OrderVO order : pagedOrders) {
            List<Map<String, Object>> productList = new ArrayList<>();
            if (order.getProductId() != null && !order.getProductId().isEmpty()) {
                String[] productIds = order.getProductId().split(",");
                String[] quantities = order.getProductQuantities() != null ? order.getProductQuantities().split(",") : new String[productIds.length];
                String[] options = order.getDetailOption() != null ? order.getDetailOption().split(",") : new String[productIds.length];

                if (quantities.length < productIds.length) quantities = Arrays.copyOf(quantities, productIds.length);
                if (options.length < productIds.length) options = Arrays.copyOf(options, productIds.length);

                for (int i = 0; i < productIds.length; i++) {
                    ProductBoardVO product = pBoardService.getProductById(productIds[i].trim());
                    if (product != null) {
                        Map<String, Object> productMap = new HashMap<>();
                        String quantity = (quantities[i] != null && !quantities[i].isEmpty()) ? quantities[i].trim() : "-";
                        String option = (options[i] != null && !options[i].isEmpty()) ? options[i].trim() : "-";

                        productMap.put("productId", product.getProductId());
                        productMap.put("name", product.getName());
                        productMap.put("quantity", quantity);
                        productMap.put("price", order.getTotalPrice());
                        productMap.put("detailOption", option);

                        String base64Image = null;
                        if (product.getImage() != null) {
                            base64Image = "data:" + product.getImageType() + ";base64," +
                                    Base64.getEncoder().encodeToString(product.getImage());
                        }
                        productMap.put("image", base64Image);

                        productList.add(productMap);
                    }
                }
            }
            orderDetailsMap.put(order.getOrderId(), productList);
        }

        // 반품 요청 매핑
        Map<Long, List<ReturnRequest>> returnRequestMap = new HashMap<>();
        for (OrderVO order : pagedOrders) {
            List<ReturnRequest> allRequests = pBoardService.getReturnRequestsByOrderId(order.getOrderId());
            for (ReturnRequest req : allRequests) {
                List<ReturnImage> images = pBoardService.getReturnImagesByReturnId(req.getReturnId());

                // 이미지 Base64 변환
                for (ReturnImage img : images) {
                    if (img.getImageData() != null) {
                        String base64 = Base64.getEncoder().encodeToString(img.getImageData());
                        img.setImageBase64(base64);
                    }
                }

                req.setImages(images);

                if (req.getProductId() != null && !req.getProductId().isEmpty()) {
                    productReturnStatusMap.put(req.getProductId().trim(), true);
                }
            }
            returnRequestMap.put(order.getOrderId(), allRequests);
        }

        // 모델 데이터
        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("username", username);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("orders", pagedOrders);
        model.addAttribute("orderDetailsMap", orderDetailsMap);
        model.addAttribute("returnRequestMap", returnRequestMap);
        model.addAttribute("totalOrderPages", totalOrderPages);
        model.addAttribute("currentOrderPage", orderPage);
        model.addAttribute("orderSize", orderSize);
        model.addAttribute("productReturnStatusMap", productReturnStatusMap);

        return "loginrefresh/logoutNav/information";
    }

	// 정보 수정 페이지
	@GetMapping("/InfoEdit")
	public String getEditPage (HttpSession session, Model model) {
		String memberId = (String) session.getAttribute("userId");
		String userId = (String) session.getAttribute("userId");
        String username = null;
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }

        // 모델에 사용자 이름 추가
        model.addAttribute("username", username);
        MemberVO memberInfo = infoService.getAllInfo(memberId);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("memberInfo", memberInfo);
		return "loginrefresh/logoutNav/InfoEdit";
	}
	
	// 수정 컨트롤러
	@PostMapping("/InfoEdit1")
	public String updateMemberInfo(@ModelAttribute MemberVO member, HttpSession session) {
	    // 세션에서 userId를 가져와서 String으로 처리
	    String loggedInMemberId = (String) session.getAttribute("userId");

	    // 가져온 userId를 member 객체에 설정
	    member.setId(loggedInMemberId);
	    
	    // 회원 정보 업데이트
	    infoService.updateMemberInfo(member);
	    return "redirect:/login/information";  // 수정 후 회원 정보 페이지로 이동 
	}
	
	// 회원 탈퇴 처리
    @GetMapping("/delete")
    public String deleteMember(HttpSession session) {
        // 세션에서 회원 ID를 가져옴
        String userId = (String) session.getAttribute("userId");

        // 회원 삭제 서비스 호출
        if (userId != null) {
            infoService.deleteMember(userId);
            session.invalidate();  // 세션 무효화
            return "redirect:/";  // 로그인 페이지로 리디렉션
        }

        // 만약 세션에 userId가 없다면 오류 페이지로
        return "redirect:/";
    }
	
    @GetMapping("/consumer")
    public String consumer(@RequestParam(defaultValue = "1") int page,
                           HttpSession session, Model model) {


        String userId = (String) session.getAttribute("userId");
        String username = null;

        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        if (userId != null) {
            username = memberService.getUserByName(userId);

            List<QBoardVO> allRequests = qBoardService.getRequestsByMemberId(userId);
            allRequests.sort((a, b) -> b.getRequestDate().compareTo(a.getRequestDate()));

            int pageSize = 3;
            int totalPages = (int) Math.ceil((double) allRequests.size() / pageSize);
            if (totalPages == 0) page = 1;
            else if (page > totalPages) page = totalPages;

            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, allRequests.size());

            List<QBoardVO> pagedRequests = allRequests.subList(fromIndex, toIndex);

            model.addAttribute("requestList", pagedRequests);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
        } else {
            model.addAttribute("requestList", new ArrayList<>());
        }

        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);

        return "loginrefresh/logoutNav/loginConsumer";
    }
    
    @PostMapping("/consultation")
    public String submitConsultation(@RequestParam String userId,
                                     @RequestParam String username,
                                     @RequestParam String consultantEmail,
                                     @RequestParam String consultationType,
                                     @RequestParam String consultantPassword,
                                     @RequestParam String consultantMessage,
                                     Model model) {

        QBoardVO request = new QBoardVO();

        // HTML에서 받은 값들을 VO에 맞게 매핑
        request.setMemberId(userId);                  // userId → memberId
        request.setConsultantName(username);          // username → consultantName
        request.setConsultantEmail(consultantEmail);
        request.setConsultationType(consultationType);
        request.setConsultantPassword(consultantPassword);
        request.setConsultantMessage(consultantMessage);
        request.setRequestDate(new Timestamp(System.currentTimeMillis()));

        qBoardService.createConsultationRequest(request);

        model.addAttribute("message", "상담 요청이 성공적으로 등록되었습니다.");
        return "redirect:/login/consumer";
    }
    
    @PostMapping("/update")
    public String cancelOrder(@RequestParam("orderId") Long orderId,
                              @RequestParam("cancelReason") String cancelReason,
                              RedirectAttributes redirectAttributes) {
        try {
            pBoardService.cancelOrder(orderId, cancelReason); // cancelReason → isCanceled
            redirectAttributes.addFlashAttribute("message", "주문이 취소가 요청 되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "취소 중 오류 발생!");
        }
        return "redirect:/login/information";
    }
    
    @PostMapping("/return")
    public ResponseEntity<String> submitReturn(
        @RequestParam("orderId") Long orderId,
        @RequestParam("memberId") String memberId,
        @RequestParam("reasonType") String reasonType,
        @RequestParam("reasonText") String reasonText,
        @RequestParam("returnProducts") List<String> productIds,
        @RequestParam("images") List<MultipartFile> images
    ) {
        // 요청서 생성
        ReturnRequest request = new ReturnRequest();
        request.setOrderId(orderId);
        request.setMemberId(memberId);
        request.setReasonType(reasonType);
        request.setReasonText(reasonText);
        request.setRequestDate(new Date(System.currentTimeMillis()));
        request.setStatus("요청");
        request.setProductId(productIds.get(0)); // 단일 상품 처리 예시
        request.setRejectReason(null);

        // 이미지 리스트 생성
        List<ReturnImage> imageList = new ArrayList<>();
        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                try {
                    ReturnImage image = new ReturnImage();
                    image.setImageName(file.getOriginalFilename());
                    image.setImageData(file.getBytes());
                    image.setUploadDate(new Date(System.currentTimeMillis()));
                    imageList.add(image);
                } catch (IOException e) {
                    // 예외 처리: 로그 남기거나 사용자에게 알림
                    e.printStackTrace();
                }
            }
        }

        // 서비스 호출
        pBoardService.createReturnRequest(request, imageList);

        String html = "<script>alert('반품 요청이 정상적으로 접수되었습니다.');"
                + "window.location.href='/login/information';</script>";

        return ResponseEntity.ok().body(html);
    }
    
    @GetMapping("/reviewlist")
    public String getReviewList(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "") String keyword,
                                Model model, HttpSession session) {

        String loginMember = (String) session.getAttribute("userId"); // 👈 로그인된 사용자 ID
        String username = null;
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        if (loginMember != null) {
            username = memberService.getUserByName(loginMember);
        }

        int pageSize = 10;
        int startRow = (page - 1) * pageSize + 1;
        int endRow = page * pageSize;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startRow", startRow);
        paramMap.put("endRow", endRow);
        paramMap.put("keyword", keyword);
        paramMap.put("userId", loginMember); // 👈 여기 추가!

        // 로그인한 사용자 리뷰만 조회
        List<PReviewVO> reviewList = pBoardService.getReviewsPaged(paramMap);

        Map<Integer, List<ReviewImageVO>> imageMap = new HashMap<>();
        for (PReviewVO review : reviewList) {
            List<ReviewImageVO> images = pBoardService.getImagesByReviewId((long) review.getReviewId());
            imageMap.put(review.getReviewId(), images);
        }

        // 사용자 리뷰 수 조회
        int totalCount = pBoardService.getTotalReviewCountByUser(paramMap); // 👈 수정된 메서드 호출
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("sidebarMenus", sidebarMenus);
	    model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("imageMap", imageMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("username", username);

        return "loginrefresh/logoutNav/myreview";
    }
}
