package com.refresh.board.service;

import com.refresh.board.dao.QBoardDAO;
import com.refresh.board.vo.BoardVO;
import com.refresh.board.vo.QBoardVO;
import com.refresh.board.vo.CommentVO;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    public List<BoardVO> getPosts(String keyword, int offset, int size) {
        return qBoardDAO.getPosts(keyword, offset, size);
    }

    public int getTotalPostCount(String keyword) {
        return qBoardDAO.getTotalPostCount(keyword);
    }
	 
	 // 게시물 상세조회
	 @Override
	 public Map<String, Object> getPagedComments(Long postId, int startRow, int endRow) {
		    List<CommentVO> comments = qBoardDAO.selectCommentsByPostIdPaged(postId, startRow, endRow);
		    int totalCount = qBoardDAO.countCommentsByPostId(postId);
		    int totalPages = (int) Math.ceil((double) totalCount / 5); // pageSize 하드코딩된 경우

		    Map<String, Object> result = new HashMap<>();
		    result.put("comments", comments);
		    result.put("totalPages", totalPages);
		 return result;
	}
	 
	 @Override
	 public BoardVO getPostById(Long postId) {
	     return qBoardDAO.getPostById(postId);
	 }
	 
	 public boolean deletePost(Long postId) {
		 return qBoardDAO.deletePost(postId) > 0;
	 }
	 
	 @Override
	 public List<QBoardVO> getRequestsByMemberId(String memberId) {
		 return qBoardDAO.getRequestsByMemberId(memberId);
	 }
	 
	 @Override
	 public BoardVO getPostByIdDelete(Long postId) {
	     return qBoardDAO.getPostByIdDelete(postId);
	 }
	 
	 @Override
	 public CommentVO getCommentById(Long commentId) {
	     return qBoardDAO.selectCommentById(commentId);
	 }

	 @Override
	 public void updateComment(CommentVO comment) {
	     qBoardDAO.updateComment(comment);
	 }

	 @Override
	 public void deleteComment(Long commentId) {
	     qBoardDAO.deleteComment(commentId);
	 }
	 
}
