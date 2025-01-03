package com.refresh.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.board.vo.CommentVO;

@Mapper
public interface QBoardDAO {
    void insertConsultationRequest(QBoardVO request);
    List<BoardVO> getPosts(); // 게시물 목록 조회
    BoardVO getPostById(int postId);  // 게시물 상세 조회
    List<CommentVO> getCommentsByPostId(int postId); // 댓글 목록 조회
}
