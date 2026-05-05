package com.blog.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.blog.service.CommentService;
import com.blog.vo.Comment;
import com.blog.vo.Result;

@RestController
public class CommentController {

	@Autowired
	CommentService commentService;
	
	@PostMapping("/comment")
	public Object savePost(HttpServletResponse response, @Valid @RequestBody Comment commentParam)  {		
		// XSS Prevention: Mengubah tag HTML/Script menjadi plain text aman
		String safeUser = HtmlUtils.htmlEscape(commentParam.getUser());
		String safeCommentStr = HtmlUtils.htmlEscape(commentParam.getComment());
		
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
		// Validasi XSS pada query pencarian
		String safeQuery = HtmlUtils.htmlEscape(query);
		return commentService.searchCommentList(postId, safeQuery);
	}

	/**
	 * Handler untuk menangkap error validasi @NotBlank / @NotNull.
	 * Jika komentar atau user kosong, server mengembalikan HTTP 400 Bad Request.
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessages = ex.getBindingResult().getFieldErrors()
			.stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.joining(", "));
		return new Result(400, "Validasi gagal: " + errorMessages);
	}
}