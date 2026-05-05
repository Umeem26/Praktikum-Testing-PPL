package com.blog.vo;

public class CommentRequest {
    private Long postId;
    private String user;
    private String comment;

    // Getters and Setters
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}