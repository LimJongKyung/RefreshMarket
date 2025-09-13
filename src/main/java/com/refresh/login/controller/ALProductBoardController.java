package com.refresh.login.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.refresh.board.service.QBoardService;
import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;
import com.refresh.login.service.LBoardService;
import com.refresh.member.service.MemberService;
import com.refresh.member.vo.MemberVO;
import com.refresh.menu.service.MenuService;
import com.refresh.menu.vo.MenuVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ALPBoard")
public class ALProductBoardController {

    private final QBoardService qboardService;
    private final MemberService memberService;
    private final LBoardService lBoardService;
    private final MenuService menuService;

    @Autowired
    public ALProductBoardController(QBoardService qboardService, MemberService memberService,
    		LBoardService lBoardService, MenuService menuService) {
        this.qboardService = qboardService;
        this.memberService = memberService;
        this.lBoardService = lBoardService;
        this.menuService = menuService;
    }

    @GetMapping("/loginBoard")
    public String loginBoard(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, HttpSession session) {
    	int offset = (page - 1) * size;
    	int totalCount = qboardService.getTotalPostCount(keyword);
	    int totalPages = (int) Math.ceil((double) totalCount / size);
    	
    	List<BoardVO> posts = qboardService.getPosts(keyword, offset, size);
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
        String userId = (String) session.getAttribute("userId");
        String username = null;

        if (userId != null) {
            // 사용자 ID를 통해 사용자 이름 가져오기
            username = memberService.getUserByName(userId);
        }

        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts found.");
        }
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("posts", posts);
        model.addAttribute("username", username);
        model.addAttribute("keyword", keyword);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
        return "loginrefresh/loginMPB/talkBoard";
    }
    
    @GetMapping("/LBDetail")
    public String getPostDetail(@RequestParam Long postId,
                                @RequestParam(defaultValue = "1") int page,
                                HttpSession session,
                                Model model) {
        int pageSize = 5; // 한 페이지당 댓글 수

        // 페이징 계산
        int startRow = (page - 1) * pageSize;
        int endRow = page * pageSize;

        // 게시글 정보
        BoardVO post = qboardService.getPostById(postId);

        // 로그인 사용자 정보
        String userId = (String) session.getAttribute("userId");
        String userName = null;
        if (userId != null) {
            userName = memberService.getUserByName(userId);
        }

        // 댓글 페이징 처리
        Map<String, Object> pagedResult = qboardService.getPagedComments(postId, startRow, endRow);
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
        model.addAttribute("username", userName);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // 이미지 처리
        if (post.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(post.getImage());
            model.addAttribute("imageBase64", base64Image);
        }

        return "loginrefresh/loginMPB/talkDetail";
    }

    // 게시글 등록 폼
    @GetMapping("/BoardC")
    public String writeForm(HttpSession session, Model model) {
    	String userId = (String) session.getAttribute("userId");
    	String userName = memberService.getUserByName(userId);
    	List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
        model.addAttribute("username", userName);
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("userId", userId);
        return "loginrefresh/loginMPB/talkBoardCreate";
    }
    
    @PostMapping("/deleteC")
    @ResponseBody
    public String deletePost(@RequestParam("postId") Long postId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        BoardVO post = qboardService.getPostByIdDelete(postId);

        if (post == null) {
            return "<script>alert('존재하지 않는 게시글입니다.'); location.href='/ALPBoard/loginBoard';</script>";
        }

        // 사용자 정보 조회
        MemberVO member = memberService.getUserById(userId);
        String grade = member.getGrade(); // 예: "사원", "운영자", "매니저"
        
        // 삭제 권한 체크: 작성자이거나 등급이 허용된 경우만 삭제 가능
        boolean isAuthor = userId.equals(post.getMemberId());
        boolean hasGradePermission = "사원".equals(grade) || "운영자".equals(grade) || "매니저".equals(grade);

        if (!(isAuthor || hasGradePermission)) {
            return "<script>alert('삭제 권한이 없습니다.'); location.href='/ALPBoard/LBDetail?postId=" + postId + "';</script>";
        }

        boolean success = qboardService.deletePost(postId);
        if (success) {
            return "<script>alert('게시글이 삭제되었습니다.'); location.href='/ALPBoard/loginBoard';</script>";
        } else {
            return "<script>alert('삭제에 실패했습니다.'); location.href='/ALPBoard/loginBoard';</script>";
        }
    }

    @PostMapping("/BoardC")
    public String write(@ModelAttribute BoardVO boardVO, HttpSession session,
                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        // 세션에서 로그인한 사용자 ID 가져오기
        String memberId = (String) session.getAttribute("userId");

        if (memberId == null) {
            return "redirect:/login"; // 로그인 안 되어 있으면 로그인 페이지로 이동
        }

        // 로그인한 사용자의 ID를 boardVO에 설정
        boardVO.setMemberId(memberId);

        // 이미지 파일이 있으면 byte[]로 변환하여 BoardVO에 설정
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                boardVO.setImage(imageFile.getBytes());  // 이미지 파일을 byte[]로 변환하여 BoardVO에 설정
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 게시글 저장
        lBoardService.insertBoard(boardVO);
        
        // 게시글 등록 후 목록 페이지로 리다이렉트
        return "redirect:/ALPBoard/loginBoard";
    }

    @PostMapping("/insert")
    public String insertComment(
            @RequestParam("postId") Integer postId,
            @RequestParam("content") String content,
            @RequestParam(value = "commentDate", required = false) String commentDateStr,
            HttpSession session, Model model) {

        if (postId == null) {
            model.addAttribute("error", "postId가 필요합니다.");
            return "redirect:/errorPage";
        }

        String memberId = (String) session.getAttribute("userId");
        if (memberId == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        CommentVO comment = new CommentVO();
        comment.setPostId(postId);
        comment.setMemberId(memberId);
        comment.setContent(content);

        // 날짜가 전달된 경우 처리
        if (commentDateStr != null) {
            comment.setCreatedDate(LocalDate.parse(commentDateStr));  // String을 LocalDate로 변환
        } else {
            // 날짜가 없을 경우 현재 날짜를 LocalDate로 설정
            comment.setCreatedDate(LocalDate.now());
        }

        lBoardService.insertComment(comment);

        return "redirect:/ALPBoard/LBDetail?postId=" + postId;
    }
    
    @PostMapping("/updateComment")
    @ResponseBody
    public ResponseEntity<?> updateComment(@RequestBody Map<String, Object> payload, HttpSession session) {
        Long commentId = Long.valueOf(payload.get("commentId").toString());
        String content = payload.get("content").toString();
        String userId = (String) session.getAttribute("userId");

        CommentVO comment = qboardService.getCommentById(commentId);
        if (comment != null && comment.getMemberId().equals(userId)) {
            comment.setContent(content);
            comment.setUpdatedDate(LocalDateTime.now().toLocalDate()); // ✅ OK
            qboardService.updateComment(comment);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam Long commentId,
                                @RequestParam Long postId,
                                HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        CommentVO comment = qboardService.getCommentById(commentId);
        if (comment != null && comment.getMemberId().equals(userId)) {
            qboardService.deleteComment(commentId);
        }
        return "redirect:/ALPBoard/LBDetail?postId=" + postId;
    }

}
