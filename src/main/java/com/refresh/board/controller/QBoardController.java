package com.refresh.board.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

@Controller
@RequestMapping("/inquiry")
public class QBoardController {
	
	private final QBoardService qBoardService;
	
	@GetMapping("/forconsumer")
    public String forConsumer() {
        return "refresh/forconsumer/forconsumer";
    }
	
	@Autowired
    public QBoardController(QBoardService qBoardService) {
        this.qBoardService = qBoardService;
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
    	if (posts == null || posts.isEmpty()) {
            System.out.println("No posts found.");
        }
        model.addAttribute("posts", posts);
        return "refresh/mainproductboard/talkboard";
    }

    
    @GetMapping("/talkDetail")
    public String getPostDetail(int postId, Model model) {
        BoardVO post = qBoardService.getPostById(postId);  // 게시물 상세 조회
        List<CommentVO> comments = qBoardService.listCommentsByPostId(postId); // 댓글 목록 조회
        model.addAttribute("post", post);  // 게시물 상세 정보를 모델에 추가
        model.addAttribute("comments", comments);  // 댓글 목록을 모델에 추가
        String dateString = "2024-12-31 14:28:31";  // 예시 날짜 문자열
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = sdf.parse(dateString);
            model.addAttribute("commentDate", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "refresh/mainproductboard/talkDetail";  // 게시물 상세 페이지로 리턴
    }
    
    /*// 게시물 상세 정보를 조회하는 메서드
    @GetMapping("/talkDetail")
    public String getPostDetail(int postId, Model model) {
        BoardVO post = qBoardService.getPostById(postId);  // 게시물 상세 조회
        model.addAttribute("post", post);  // 게시물 상세 정보를 모델에 추가
        return "refresh/mainproductboard/talkDetail";  // 게시물 상세 페이지로 리턴
    }
    
    // 댓글 목록을 가져오는 메서드
    @GetMapping("/comments/{postId}")
    public String getComment(@PathVariable int postId, Model model) {
        List<CommentVO> comments = qBoardService.listCommentsByPostId(postId); // 서비스에서 댓글 목록 반환
        model.addAttribute("comments", comments);
        return "template/refresh/fragments/comment"; // comments.html로 반환
    }*/
}
