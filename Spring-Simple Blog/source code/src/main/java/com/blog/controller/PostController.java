package com.blog.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.blog.dto.PostRequest;
import com.blog.service.PostService;
import com.blog.vo.Post;
import com.blog.vo.Result;

@RestController
public class PostController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	PostService postService;
	
	@GetMapping("/post")
	public Post getPost(@RequestParam("id") Long id) {
		return postService.getPost(id);
	}
	
	@GetMapping("/posts")
	public List<Post> getPosts() {
		return postService.getPosts();
	}
	
	@GetMapping("/posts/updtdate/asc")
	public List<Post> getPostsOrderByUpdtAsc() {
		return postService.getPostsOrderByUpdtAsc();
	}
	
	@GetMapping("/posts/regdate/desc")
	public List<Post> getPostsOrderByRegDesc() {
		return postService.getPostsOrderByRegDesc();
	}
	
	@GetMapping("/posts/search/title")
	public List<Post> searchByTitle(@RequestParam("query") String query) {
		// Validasi XSS pada query pencarian
		String safeQuery = HtmlUtils.htmlEscape(query);
		return postService.searchPostByTitle(safeQuery);
	}
	
	@GetMapping("/posts/search/content")
	public List<Post> searchByContent(@RequestParam("query") String query) {
		// Validasi XSS pada query pencarian
		String safeQuery = HtmlUtils.htmlEscape(query);
		return postService.searchPostByContent(safeQuery);
	}
	
	@PostMapping("/post")
	public Object savePost(HttpServletResponse response, @Valid @RequestBody PostRequest postParam)  {		
		// XSS Prevention: Mengubah tag HTML/Script menjadi plain text aman
		String safeUser = HtmlUtils.htmlEscape(postParam.getUser());
		String safeTitle = HtmlUtils.htmlEscape(postParam.getTitle());
		String safeContent = HtmlUtils.htmlEscape(postParam.getContent());
		
		Post post = new Post(safeUser, safeTitle, safeContent);
		boolean isSuccess = postService.savePost(post);
		
		if(isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}
	
	@DeleteMapping("/post")
	public Object deletePost(HttpServletResponse response, @RequestParam("id") Long id)  {
		boolean isSuccess = postService.deletePost(id);
		log.info("id ::: " + id);
		
		if(isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}
	
	@PutMapping("/post")
	public Object modifyPost(HttpServletResponse response, @Valid @RequestBody PostRequest postParam)  {		
		// XSS Prevention: Mengubah tag HTML/Script menjadi plain text aman
		String safeTitle = HtmlUtils.htmlEscape(postParam.getTitle());
		String safeContent = HtmlUtils.htmlEscape(postParam.getContent());
		
		Post post = new Post(postParam.getId(), safeTitle, safeContent);
		boolean isSuccess = postService.updatePost(post);
				
		if(isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new Result(500, "Fail");
		}
	}

	/**
	 * Handler untuk menangkap error validasi @NotBlank / @Size.
	 * Jika field wajib kosong, server mengembalikan HTTP 400 Bad Request
	 * beserta daftar pesan error yang deskriptif.
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