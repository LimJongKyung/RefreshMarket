package com.refresh.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.CommentVO;
import com.refresh.login.dao.LBoardDAO;

@Service
public class LBoardServiceImpl implements LBoardService {
	
	private final LBoardDAO lBoardDAO;

    // DAO 의존성 주입
    @Autowired
    public LBoardServiceImpl(LBoardDAO lBoardDAO) {
        this.lBoardDAO = lBoardDAO;
    }

    @Override
    public int insertBoard(BoardVO boardVO) {
        return lBoardDAO.insertBoard(boardVO);
    }
    
    @Override
    public void insertComment(CommentVO comment) {
        lBoardDAO.insertComment(comment);
    }
}
