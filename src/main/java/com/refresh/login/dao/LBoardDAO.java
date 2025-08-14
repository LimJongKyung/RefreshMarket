package com.refresh.login.dao;

import org.apache.ibatis.annotations.Mapper;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;

@Mapper
public interface LBoardDAO {
	 // 글 작성 메서드
	int insertBoard(BoardVO boardVO);
	void insertComment(CommentVO commentVO);
}
