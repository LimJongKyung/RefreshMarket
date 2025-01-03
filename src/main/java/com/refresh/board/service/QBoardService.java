package com.refresh.board.service;

import java.util.List;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.board.vo.CommentVO;

public interface QBoardService {
    void createConsultationRequest(QBoardVO request);
    List<BoardVO> getPosts(); // 게시물 리스트
    BoardVO getPostById(int postId);  // 게시물 상세 조회
    List<CommentVO> listCommentsByPostId(int postId);  // 댓글 목록 조회
}