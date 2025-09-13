package com.refresh.board.controller;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.refresh.board.service.QBoardService;
import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/inquiry")
public class QBoardController {
	
	private final QBoardService qBoardService;
	private final MenuService menuService;

	@Autowired
    public QBoardController(QBoardService qBoardService, MenuService menuService) {
        this.qBoardService = qBoardService;
        this.menuService = menuService;
    }
	
	@GetMapping("/forconsumer")
    public String forConsumer(Model model) {
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        return "refresh/forconsumer/forconsumer";
    }
	
	@PostMapping("/success")
	public ResponseEntity<String> createConsultationRequest(@RequestBody QBoardVO request, HttpSession session) {
	    try {
	        // 세션에서 사용자 ID 확인
	        String memberId = (session.getAttribute("id") != null) 
	            ? session.getAttribute("id").toString() 
	            : "비회원";

	        // QBoardVO에 memberId 설정
	        request.setMemberId(memberId);

	        // 서비스 호출
	        qBoardService.createConsultationRequest(request);

	        return ResponseEntity.ok("상담 요청이 성공적으로 처리되었습니다. 상담 내용은 이메일을 통해 확인하실 수 있습니다!");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("상담 요청 처리 중 오류가 발생했습니다: " + e.getMessage());
	    }
	}
    
	@GetMapping("/talkboard")
	public String getPosts(@RequestParam(required = false) String keyword,
	                       @RequestParam(defaultValue = "1") int page,
	                       @RequestParam(defaultValue = "10") int size,
	                       Model model) {

	    // 페이징 계산
	    int offset = (page - 1) * size;

	    // 검색 + 페이징 처리된 게시글 목록 가져오기
	    List<BoardVO> posts = qBoardService.getPosts(keyword, offset, size);
	    int totalCount = qBoardService.getTotalPostCount(keyword);
	    int totalPages = (int) Math.ceil((double) totalCount / size);

	    // 메뉴
	    List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
	    List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

	    // 모델에 데이터 추가
	    model.addAttribute("posts", posts);
	    model.addAttribute("sidebarMenus", sidebarMenus);
	    model.addAttribute("headerMenus", headerMenus);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);

	    return "refresh/mainproductboard/talkboard";
	}
    
    @GetMapping("/talkDetail")
    public String getPostDetail(@RequestParam Long postId,
            @RequestParam(defaultValue = "1") int page,
            HttpSession session,
            Model model) {
		int pageSize = 5; // 한 페이지당 댓글 수
		
		// 페이징 계산
		int startRow = (page - 1) * pageSize;
		int endRow = page * pageSize;
		
		// 게시글 정보
		BoardVO post = qBoardService.getPostById(postId);
		
		// 로그인 사용자 정보
		String userId = (String) session.getAttribute("userId");
		
		// 댓글 페이징 처리
		Map<String, Object> pagedResult = qBoardService.getPagedComments(postId, startRow, endRow);
		@SuppressWarnings("unchecked")
		List<CommentVO> comments = (List<CommentVO>) pagedResult.get("comments");
		int totalPages = (int) pagedResult.get("totalPages");
		
		// 메뉴 정보
		List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
		List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
		
		// 모델에 데이터 추가
		model.addAttribute("sidebarMenus", sidebarMenus);
		model.addAttribute("headerMenus", headerMenus);
		model.addAttribute("post", post);
		model.addAttribute("comments", comments);
		model.addAttribute("postId", postId);
		model.addAttribute("userId", userId);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		
		// 이미지 처리
		if (post.getImage() != null) {
		String base64Image = Base64.getEncoder().encodeToString(post.getImage());
		model.addAttribute("imageBase64", base64Image);
		}
	
	return "refresh/mainproductboard/talkDetail";
	}
 
}
