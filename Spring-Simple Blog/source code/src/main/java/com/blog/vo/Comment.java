package com.blog.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "comment", indexes = {
    @Index(name = "idx_comment_post_id", columnList = "postId")
})
public class Comment {

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
	private Long id;
	
	@Column(name="postId")
	private Long postId;
    
    @Column(name="user")
	private String user;
    
    @Column(name="comment")
	private String comment;
    
    @JsonFormat(pattern = "dd MMMM yyyy HH:mm", timezone = "Asia/Jakarta")
    @Column(name="regDate")
	private LocalDateTime regDate;

	public Comment() {
	}

	public Comment(Long postId, String user, String comment) {
		this.postId = postId;
		this.user = user;
		this.comment = comment;
		this.regDate = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}
}
