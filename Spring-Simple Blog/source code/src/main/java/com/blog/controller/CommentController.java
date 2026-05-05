package com.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.blog.service.CommentService;
import com.blog.vo.Comment;
import com.blog.vo.CommentRequest;
import com.blog.vo.Result;

@RestController
public class CommentController {

	private final CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@PostMapping("/comment")
	public Object savePost(HttpServletResponse response, @RequestBody CommentRequest commentParam)  {		
		// XSS Prevention dengan Null Check
		String safeUser = commentParam.getUser() == null ? null : HtmlUtils.htmlEscape(commentParam.getUser());
		String safeCommentStr = commentParam.getComment() == null ? null : HtmlUtils.htmlEscape(commentParam.getComment());
		
		Comment comment = new Comment(commentParam.getPostId(), safeUser, safeCommentStr);
		boolean isSuccess = commentService.saveComment(comment);
		
		if(isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}
	
	@GetMapping("/comments")
	public List<Comment> getComments(@RequestParam("post_id") Long postId) {
		return commentService.getCommentList(postId);
	}
	
	@GetMapping("/comment")
	public Comment getComment(@RequestParam("id") Long id) {
		return commentService.getComment(id);
	}
	
	@DeleteMapping("/comment")
	public Object deleteComments(HttpServletResponse response, @RequestParam("id") Long id) {
		boolean isSuccess = commentService.deleteComment(id);
		
		if(isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}
	
	@GetMapping("/comments/search")
	public List<Comment> searchComments(@RequestParam("post_id") Long postId, @RequestParam("query") String query) {
		// Validasi XSS pada query pencarian dengan Null check
		String safeQuery = query == null ? null : HtmlUtils.htmlEscape(query);
		return commentService.searchCommentList(postId, safeQuery);
	}
}