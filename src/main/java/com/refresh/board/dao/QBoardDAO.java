package com.refresh.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.board.vo.CommentVO;

@Mapper
public interface QBoardDAO {
    void insertConsultationRequest(QBoardVO request);
    
    List<BoardVO> getPosts(@Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("size") int size);

    int getTotalPostCount(@Param("keyword") String keyword);
    
    List<CommentVO> selectCommentsByPostIdPaged(@Param("postId") Long postId,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);

    int countCommentsByPostId(@Param("postId") Long postId);
    
    List<CommentVO> getCommentsByPostId(int postId); // 댓글 목록 조회
    
    BoardVO getPostById(@Param("postId") Long postId);
    
    int deletePost(@Param("postId") Long postId);
    
    List<QBoardVO> getRequestsByMemberId(String memberId);
    
    BoardVO getPostByIdDelete(@Param("postId") Long postId);
    
    CommentVO selectCommentById(Long commentId);
    void updateComment(CommentVO comment);
    void deleteComment(Long commentId);
}
