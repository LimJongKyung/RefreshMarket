package com.refresh.board.service;

import com.refresh.board.dao.QBoardDAO;
import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.board.vo.CommentVO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QBoardServiceImpl implements QBoardService {

    private final QBoardDAO qBoardDAO;

    @Autowired
    public QBoardServiceImpl(QBoardDAO qBoardDAO) {
        this.qBoardDAO = qBoardDAO;
    }

    @Override
    public void createConsultationRequest(QBoardVO request) {
        qBoardDAO.insertConsultationRequest(request); // DAO 호출
    }
    
     // 게시물 리스트
	 @Override
	 public List<BoardVO> getPosts() {
		 return qBoardDAO.getPosts();
	 }
	 
	 // 게시물 상세조회
	 @Override
	 public BoardVO getPostById(int postId) {
	     return qBoardDAO.getPostById(postId); 
	 }
	 
	 // 댓글 리스트
	 @Override
	 public List<CommentVO> listCommentsByPostId(int postId) {
	     return qBoardDAO.getCommentsByPostId(postId);
	 }
}
