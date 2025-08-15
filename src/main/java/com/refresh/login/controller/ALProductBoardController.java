package com.refresh.login.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.refresh.board.service.QBoardService;
import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;
import com.refresh.login.service.LBoardService;
import com.refresh.member.service.MemberService;
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
    public String loginBoard(HttpSession session, Model model) {
        List<BoardVO> posts = qboardService.getPosts();
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
        return "loginrefresh/loginMPB/talkBoard";
    }
    
    @GetMapping("/LBDetail")
    public String getPostDetail(@RequestParam int postId, HttpSession session, Model model) {
        BoardVO post = qboardService.getPostById(postId);  
        String userId = (String) session.getAttribute("userId");
        String userName = memberService.getUserByName(userId);
        List<CommentVO> comments = qboardService.listCommentsByPostId(postId); 
        List<MenuVO> sidebarMenus = menuService.getMenusByPosition("sidebar");
        List<MenuVO> headerMenus = menuService.getMenusByPosition("header");
        
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("headerMenus", headerMenus);
        model.addAttribute("post", post);  
        model.addAttribute("comments", comments);  
        model.addAttribute("postId", postId);  // 👉 postId를 모델에 저장
        model.addAttribute("userId", userId);
        model.addAttribute("username", userName);
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

}
