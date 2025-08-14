package com.refresh.board.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CommentVO {
	private int commentId;       // 댓글 ID
    private int postId;          // 게시글 ID
    private String memberId;     // 작성자 ID
    private String content;      // 댓글 내용
    private LocalDate createdDate;  // 작성일 (String 형식으로)
    private LocalDate updatedDate;  // 수정일
    private int likeCount;       // 좋아요 수
    private Integer parentId;    // 부모 댓글 ID (대댓글)

    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
