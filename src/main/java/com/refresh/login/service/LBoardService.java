package com.refresh.login.service;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;

public interface LBoardService {
	// 글 작성 메서드
	 int insertBoard(BoardVO boardVO);
	 void insertComment(CommentVO comment);
}
