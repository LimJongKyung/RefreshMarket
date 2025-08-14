package com.refresh.board.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.refresh.board.service.QBoardService;
import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

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
    public ResponseEntity<String> createConsultationRequest(@RequestBody QBoardVO request) {
        try {
            qBoardService.createConsultationRequest(request);
            return ResponseEntity.ok("상담 요청이 성공적으로 처리되었습니다. 상담 내용은 이메일을 통해 확인하실 수 있습니다!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("상담 요청 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    // 일반 게시판
    @GetMapping("/talkboard")
    public String getPosts(Model model) {
    	List<BoardVO> posts = qBoardService.getPosts();
    	List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
    	if (posts == null || posts.isEmpty()) {
            System.out.println("No posts found.");
        }

        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("posts", posts);
        return "refresh/mainproductboard/talkboard";
    }

    
    @GetMapping("/talkDetail")
    public String getPostDetail(int postId, Model model) {
    	BoardVO post = qBoardService.getPostById(postId);  // 게시물 상세 조회
	    List<CommentVO> comments = qBoardService.listCommentsByPostId(postId); // 댓글 목록 조회
	    List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");

        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
	    model.addAttribute("post", post);  // 게시물 상세 정보를 모델에 추가
	    model.addAttribute("comments", comments);  // 댓글 목록을 모델에 추가

	    // 이미지가 존재하는 경우 base64로 변환
	    if (post.getImage() != null) {
	        String base64Image = Base64.getEncoder().encodeToString(post.getImage());
	        model.addAttribute("imageBase64", base64Image);  // base64로 변환된 이미지를 모델에 추가
	    } else {
		   System.out.println("이미지 있음");
	   }
        return "refresh/mainproductboard/talkDetail";  // 게시물 상세 페이지로 리턴
    }
 
}
