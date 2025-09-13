package com.refresh.board.service;

import java.util.List;
import java.util.Map;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;
import com.refresh.board.vo.QBoardVO;

public interface QBoardService {
    void createConsultationRequest(QBoardVO request);
    List<BoardVO> getPosts(String keyword, int offset, int size); // 게시물 리스트
    int getTotalPostCount(String keyword);
    
    BoardVO getPostById(Long postId);  // 게시물 상세 조회
    
    Map<String, Object> getPagedComments(Long postId, int startRow, int endRow);  // 댓글 목록 조회
    
    boolean deletePost(Long postId);
    
    List<QBoardVO> getRequestsByMemberId(String memberId);
    
	BoardVO getPostByIdDelete(Long postId);
	
	CommentVO getCommentById(Long commentId);
	void updateComment(CommentVO comment);
    void deleteComment(Long commentId);
}