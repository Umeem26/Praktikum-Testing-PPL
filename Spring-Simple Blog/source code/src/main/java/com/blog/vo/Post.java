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
@Table(name = "post", indexes = {
    @Index(name = "idx_post_updt_date", columnList = "updtDate"),
    @Index(name = "idx_post_reg_date", columnList = "regDate")
})
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
	private Long id;
    
    @Column(name="user")
	private String user;
    
    @Column(name="title")
	private String title;
    
    @Column(name="content")
	private String content;
    
    @JsonFormat(pattern = "dd MMMM yyyy HH:mm", timezone = "Asia/Jakarta")
    @Column(name="regDate")
	private LocalDateTime regDate;
    
    @JsonFormat(pattern = "dd MMMM yyyy HH:mm", timezone = "Asia/Jakarta")
    @Column(name="updtDate")
	private LocalDateTime updtDate;

	public Post() {
	}
	
	public Post(String user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.regDate = LocalDateTime.now();
		this.updtDate = LocalDateTime.now();
	}

	public Post(Long id, String user, String title, String content) {
		super();
		this.id = id;
		this.user = user;
		this.title = title;
		this.content = content;
		this.regDate = LocalDateTime.now();
		this.updtDate = LocalDateTime.now();
	}

	public Post(Long id, String title, String content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public LocalDateTime getUpdtDate() {
		return updtDate;
	}

	public void setUpdtDate(LocalDateTime updtDate) {
		this.updtDate = updtDate;
	}

}
